package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.ArrayList;
import java.util.List;

public class BalloonGameMode implements GameMode {

    protected int collectibleCount = 0;
    protected List<Collectible> balloonList;

    public BalloonGameMode(ResourceRequirements resourceRequirements){
        NotificationManager.addObserver(CollisionManager.collectibleCollisionId, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                collectibleCount++;
                System.out.println("Baloons: " + collectibleCount);
                if(event.data.getObject1().getName().equals(CollisionManager.collectibleCollisionId)){
                    event.data.getObject1().setEnabled(false);
                }else{
                    event.data.getObject2().setEnabled(false);
                }
            }
        });

        balloonList = new ArrayList<Collectible>();
        for(int i = 3500; i > 1000 ; i-=100){
            float x = (float)Math.cos((i-1000)/2500f*10*Math.PI)*75;
            float z = (float)Math.sin((i-1000)/2500f*10*Math.PI)*75;
            Collectible c = new Collectible(resourceRequirements, new Vector(x,i,z));
            balloonList.add(c);
        }
    }

    public void update(float deltaTime){
        for (Collectible c : balloonList){
            c.update(deltaTime);
        }
    }
}
