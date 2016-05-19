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

    private int collectedBalloonsCount = 0;

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
        NotificationManager.addObserver(LandedState.playerHasStopped, new NotificationManager.EventHandler<Object>() {

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
            float y = ((float) i) * 100.0f + 1000.0f;

            Collectible c = new Collectible(resourceRequirements, new Vector(x, y, z));
            balloonList.add(c);
        }
    }

    private void balloonCollision(CollisionManager.CollisionData collisionData) {
        collisionData.getOtherObject().setEnabled(false);
        collisionData.getOtherObject().setParentEnabled(false);
        collectedBalloonsCount += 1;
        PositionedSound balloonPositionedSound = new PositionedSound(balloonSound, collisionData.getJumperObject().getPosition());
        balloonPositionedSound.play();
    }

    private void obstacleCollision(CollisionManager.CollisionData collisionData) {
        // TODO: Implement this some way!
    }

    public void update(float deltaTime) {
        for (Collectible c : balloonList) {
            c.update(deltaTime);
        }

        if(gameIsFinished) {
            String endText = "You caught " + collectedBalloonsCount + " out of " + totalBalloonCount + " possible!";
            String playAgainText = "Tap the screen to play again";

            RenderQueue.addGUITask(new GUITextTask(endText, new Vector(1, 0, 0), new Vector(0.5f, 0, .6f), true));
            RenderQueue.addGUITask(new GUITextTask(playAgainText, new Vector(1, 0, 0), new Vector(0.5f, 0, .4f), true));
        }
    }

    @Override
    public String toString() {
        return "BalloonGameMode{" +
                "collectedBalloonsCount=" + collectedBalloonsCount +
                ", totalBalloonCount=" + totalBalloonCount +
                '}';
    }
}
