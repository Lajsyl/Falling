package dat367.falling.core;

public class BalloonGameMode extends GameMode {

    protected int collectibleCount = 0;

    public BalloonGameMode(World world){
        super(world);
        NotificationManager.addObserver(CollisionManager.collectibleCollisionId, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                System.out.println("Jag vet att du krockar!");
                collectibleCount++;
                if(event.data.getObject1().getName().equals(CollisionManager.collectibleCollisionId)){
                    event.data.getObject1().setEnabled(false);
                }else{
                    event.data.getObject2().setEnabled(false);
                }
            }
        });
    }
}
