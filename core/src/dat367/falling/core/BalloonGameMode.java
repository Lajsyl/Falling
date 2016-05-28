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

    public static final float BALLOON_MAX_DRAW_DISTANCE = 500.0f;
    public static final float BALLOON_FADE_OUT_DISTANCE = 50.0f;

    private int balloonCombo = 0;
    private int score = 0;

    private boolean gameIsFinished = false;

    private List<Sound> balloonSounds = new ArrayList<Sound>();
    private Sound explosionSound = new Sound("explosion.wav");
    private final int NUMBER_OF_BALLOON_SOUNDS = 13;

    public BalloonGameMode(ResourceRequirements resourceRequirements, BalloonLevel level) {

        initBalloonSounds(resourceRequirements);
        resourceRequirements.require(explosionSound);

        // Listen for all relevant collision events
        NotificationManager.getDefault().addObserver(CollisionManager.COLLECTIBLE_COLLISION_EVENT_ID, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                balloonCollision(event.data);
            }
        });

        NotificationManager.getDefault().addObserver(CollisionManager.OBSTACLE_COLLISION_EVENT_ID, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                obstacleCollision(event.data);
            }
        });

        // When the player has landed and stopped moving the game is finished
        NotificationManager.getDefault().addObserver(LandedState.PLAYER_HAS_STOPPED_EVENT_ID, new NotificationManager.EventHandler<Object>() {

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
//        setGameElementsEnabled(false);

        // When the player jumps out of the plane, enable collectibles and obstacles
        NotificationManager.getDefault().addObserver(PreJumpState.PLAYER_HAS_JUMPED_EVENT_ID, new NotificationManager.EventHandler<Object>() {
            @Override
            public void handleEvent(NotificationManager.Event<Object> event) {
                setGameElementsEnabled(true);
            }
        });
    }

    private void setGameElementsEnabled(boolean enabled) {
        for (Interactable interactable : level.getInteractableList()) {
            interactable.setEnabled(enabled);
        }
    }

    private void balloonCollision(CollisionManager.CollisionData collisionData) {
        collisionData.getOtherObject().setEnabled(false);
        collisionData.getOtherObject().setParentEnabled(false);
        balloonCombo += 1;
        score += 100*balloonCombo;
        System.out.println(score);

        PositionedSound balloonPositionedSound = new PinnedPositionedSound(getBalloonSound(balloonCombo), collisionData.getJumperObject().getParent(), new Vector(0, 1, 0));
        balloonPositionedSound.play();
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
        // Bounce on mine
        Jumper jumper = (Jumper)collisionData.getJumperObject().getParent();
        Mine mine = (Mine)collisionData.getOtherObject().getParent();
        Vector yBounce = new Vector(0, 220.0f, 0);
        Vector playerPos = collisionData.getJumperObject().getPosition();
        Vector obstaclePos = collisionData.getOtherObject().getPosition();
        Vector xzBounce = playerPos.sub(obstaclePos).projectOntoPlaneXZ().scale(100.0f);//new Vector(jumper.getVelocity().getX(), 0, jumper.getVelocity().getZ());
        jumper.setVelocity(xzBounce.add(yBounce));
        PositionedSound explosionPositionedSound = new PositionedSound(explosionSound, collisionData.getOtherObject().getPosition());
        explosionPositionedSound.play();
        // --------------------------------

        collisionData.getOtherObject().setEnabled(false);
        collisionData.getOtherObject().setParentEnabled(false);
        balloonCombo = 0;
        System.out.println("mine hit");
    }

    public void update(float deltaTime, World world) {
        level.update(deltaTime, world.getJumper());
        if(gameIsFinished) {
            String endText = "  Your score was:\n\n\n\n  Tap the screen\n    to play again";
            RenderQueue.getDefault().addGUITask(new GUITextTask(endText, new Vector(1, 1, 1), new Vector(0.5f, 0, .61f), true, false));
            String scoreText = Integer.toString(1200);
            RenderQueue.getDefault().addGUITask(new GUITextTask(scoreText, new Vector(1, 1, 1), new Vector(0.505f, 0, .565f), true, true));
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
