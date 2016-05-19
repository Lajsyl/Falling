package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.GUITextTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;
import dat367.falling.platform_abstraction.Sound;

import java.util.ArrayList;
import java.util.List;

public class BalloonGameMode implements GameMode {

    private final int totalBalloonCount = 25;
    private List<Collectible> balloonList = new ArrayList<Collectible>(totalBalloonCount);
    private final int totalObstacleCount = 25;
    private List<Obstacle> obstacleList = new ArrayList<Obstacle>(totalObstacleCount);

    private int balloonCombo = 0;
    private int score = 0;

    private boolean gameIsFinished = false;

    private Sound balloonSound = new Sound("balloon1.wav");

    public BalloonGameMode(ResourceRequirements resourceRequirements) {

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

        for (int i = 0; i < totalBalloonCount; i++) {
            float step = (float) i;
            float x = (float)Math.cos(step) * 60;
            float z = (float)Math.sin(step) * 60;

            // Every 100 meters from 1000m and up.
            float y = step * 100.0f + 1000.0f;

            Collectible c = new Collectible(resourceRequirements, new Vector(x, y, z));
            balloonList.add(c);
        }

        for (int i = 0; i < totalObstacleCount; i++){
            float step = (float) i;
            float x = (float)Math.cos(step) * 30 + 100;
            float z = (float)Math.sin(step) * 30 + 100;

            // Every 100 meters from 1000m and up.
            float y = step * 100.0f + 1000.0f + 100;

            Obstacle o = new Obstacle(resourceRequirements, new Vector(x, y, z));
            obstacleList.add(o);
        }
    }

    private void balloonCollision(CollisionManager.CollisionData collisionData) {
        collisionData.getOtherObject().setEnabled(false);
        balloonCombo += 1;
        score += 100*balloonCombo;
        System.out.println(score);

        PositionedSound balloonPositionedSound = new PositionedSound(balloonSound, collisionData.getJumperObject().getPosition());
        balloonPositionedSound.play();
    }

    private void obstacleCollision(CollisionManager.CollisionData collisionData) {
        collisionData.getOtherObject().setEnabled(false);
        balloonCombo = 0;
        System.out.println("mine" + balloonCombo);
    }

    public void update(float deltaTime) {
        for (Collectible c : balloonList) {
            c.update(deltaTime);
        }

        for (Obstacle o : obstacleList) {
            o.update(deltaTime);
        }

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
                ", totalBalloonCount=" + totalBalloonCount +
                "balloonCombo=" + balloonCombo +
                ", score=" + score +
                '}';
    }
}
