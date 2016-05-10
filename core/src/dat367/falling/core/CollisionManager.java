package dat367.falling.core;


import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    static List<Collidable> collidables = new ArrayList<Collidable>();
    public static Collidable jumper;
    public static final String id = "CollisionEvent";
    public static final String collectibleCollisionId = "collectibleCollisionEvent";

    public static void addCollider(Collidable collidable){
        if(collidable.getName().equals(Jumper.NAME)){
            assert jumper == null;
            jumper = collidable;
        }else
        collidables.add(collidable);
    }

    public static void clear(){
        collidables.clear();
        jumper = null;
    }

    public static void update(float deltaTime){
        for(Collidable c : collidables){
            if(jumper.collidesWith(c)){
                if (c.getName().equals("Collectible")) {
                    NotificationManager.registerEvent(collectibleCollisionId, new CollisionData(jumper, c));
                }else{
                    NotificationManager.registerEvent(id, new CollisionData(jumper, c));
                }
            }
        }
    }

    public static class CollisionData{
        private final Collidable object1;
        private final Collidable object2;

        public CollisionData(Collidable object1, Collidable object2){
            this.object1 = object1;
            this.object2 = object2;
        }

        public Collidable getObject1() {
            return object1;
        }

        public Collidable getObject2() {
            return object2;
        }
    }
}
