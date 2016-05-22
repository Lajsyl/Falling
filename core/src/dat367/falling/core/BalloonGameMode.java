package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.GUITextTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;
import dat367.falling.platform_abstraction.Sound;

import java.util.ArrayList;
import java.util.List;

public class BalloonGameMode implements GameMode {

    private BalloonLevel level;

    private int balloonCombo = 0;
    private int score = 0;

    private boolean gameIsFinished = false;

    private List<Sound> balloonSounds = new ArrayList<Sound>();
    private final int NUMBER_OF_BALLOON_SOUNDS = 13;

    public BalloonGameMode(ResourceRequirements resourceRequirements, BalloonLevel level) {

        initBalloonSounds(resourceRequirements);

        // Listen for all relevant collision events

        NotificationManager.addObserver(CollisionManager.COLLECTIBLE_COLLISION_EVENT_ID, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                balloonCollision(event.data);
            }
        });

        NotificationManager.addObserver(CollisionManager.OBSTACLE_COLLISION_EVENT_ID, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                obstacleCollision(event.data);
            }
        });

        // When the player has landed and stopped moving the game is finished
        NotificationManager.addObserver(LandedState.PLAYER_HAS_STOPPED_EVENT_ID, new NotificationManager.EventHandler<Object>() {

            @Override
            public void handleEvent(NotificationManager.Event<Object> event) {
                gameIsFinished = true;
            }
        });


        this.level = level;
        level.create();
        // Wait with enabling game elements rendering
        // until after the player jumps out of the plane
        // in order to improve frame rate
        setGameElementsEnabled(false);

        // When the player jumps out of the plane, enable collectibles and obstacles
        NotificationManager.addObserver(PreJumpState.PLAYER_HAS_JUMPED_EVENT_ID, new NotificationManager.EventHandler<Object>() {
            @Override
            public void handleEvent(NotificationManager.Event<Object> event) {
                setGameElementsEnabled(true);
            }
        });
    }

    private void setGameElementsEnabled(boolean enabled) {
        for (Collectible collectible : level.getBalloonList()) {
            collectible.setEnabled(enabled);
        }
        for (Obstacle obstacle : level.getObstacleList()) {
            obstacle.setEnabled(enabled);
        }
    }

    private void balloonCollision(CollisionManager.CollisionData collisionData) {
        collisionData.getOtherObject().setEnabled(false);
        collisionData.getOtherObject().setParentEnabled(false);
        balloonCombo += 1;
        score += 100*balloonCombo;
        System.out.println(score);

        PositionedSound balloonSound = new PinnedPositionedSound(getBalloonSound(balloonCombo), collisionData.getJumperObject().getParent(), new Vector(0, 1, 0));
        balloonSound.play();
    }

    private void initBalloonSounds(ResourceRequirements resourceRequirements) {
        for (int i = 1; i <= NUMBER_OF_BALLOON_SOUNDS; i++) {
            Sound sound = new Sound("balloon" + i + ".wav");
            balloonSounds.add(sound);
            resourceRequirements.require(sound);
        }
    }

    private Sound getBalloonSound(int note) {
        int soundIndex = note - 1;
        if (soundIndex >= balloonSounds.size()) {
            return balloonSounds.get(balloonSounds.size() - 1);
        } else {
            return balloonSounds.get(soundIndex);
        }
    }

    private void obstacleCollision(CollisionManager.CollisionData collisionData) {
        collisionData.getOtherObject().setEnabled(false);
        collisionData.getOtherObject().setParentEnabled(false);
        if (balloonCombo < 15) {
            balloonCombo = 0;
        }else{
            balloonCombo -= 15;
        }
        System.out.println("mine hit");
    }

    public void update(float deltaTime) {
        /*for (Collectible c : balloonList) {
            c.update(deltaTime);
        }

        for (Obstacle o : obstacleList) {
            o.update(deltaTime);
        }*/

        level.update(deltaTime);
        if(gameIsFinished) {
            String endText = "Your score was: " + score;
            String playAgainText = "Tap the screen to play again";

            RenderQueue.addGUITask(new GUITextTask(endText, new Vector(1, 0, 0), new Vector(0.5f, 0, .6f), true));
            RenderQueue.addGUITask(new GUITextTask(playAgainText, new Vector(1, 0, 0), new Vector(0.5f, 0, .4f), true));
        }
    }

    @Override
    public String toString() {
        return "BalloonGameMode{" +
                "level=" + level +
                "balloonCombo=" + balloonCombo +
                ", score=" + score +
                '}';
    }
}
