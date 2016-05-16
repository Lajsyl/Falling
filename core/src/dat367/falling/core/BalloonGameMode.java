package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.GUITask;
import dat367.falling.platform_abstraction.GUITextTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.ArrayList;
import java.util.List;

public class BalloonGameMode implements GameMode {

    protected int collectibleCount = 0;
    protected int numberOfBalloons = 0;
    protected List<Collectible> balloonList;
    private boolean renderText = false;

    public BalloonGameMode(ResourceRequirements resourceRequirements){
        NotificationManager.addObserver(CollisionManager.collectibleCollisionId, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                collectibleCount++;
                if(event.data.getObject1().getName().equals(CollisionManager.collectibleCollisionId)){
                    event.data.getObject1().setEnabled(false);
                }else{
                    event.data.getObject2().setEnabled(false);
                }
            }
        });
        NotificationManager.addObserver(LandedState.playerHasStopped, new NotificationManager.EventHandler<Object>() {

            @Override
            public void handleEvent(NotificationManager.Event<Object> event) {
                renderText = true;
            }
        });

        balloonList = new ArrayList<Collectible>();
        for(int i = 3500; i > 1000 ; i-=100){
            float x = (float)Math.cos((i-1000)/2500f*10*Math.PI)*75;
            float z = (float)Math.sin((i-1000)/2500f*10*Math.PI)*75;
            Collectible c = new Collectible(resourceRequirements, new Vector(x,i,z));
            balloonList.add(c);
            numberOfBalloons++;
        }
    }

    public void update(float deltaTime){
        for (Collectible c : balloonList){
            c.update(deltaTime);
        }

        if(renderText) {
            String text = "You cought " + collectibleCount + " of " + numberOfBalloons;
            RenderQueue.addGUITask(new GUITextTask(text, new Vector(1, 0, 0), new Vector(0.5f, 0.5f, .5f), true));
        }
    }

    @Override
    public String toString() {
        return "BalloonGameMode{" +
                "collectibleCount=" + collectibleCount +
                ", numberOfBalloons=" + numberOfBalloons +
                '}';
    }
}
