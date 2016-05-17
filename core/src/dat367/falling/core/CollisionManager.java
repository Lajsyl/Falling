package dat367.falling.core;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    static List<Collidable> collidables = new ArrayList<Collidable>();
    public static Collidable jumper;

    public static final String COLLISION_EVENT_ID = "CollisionEvent";
    public static final String COLLECTIBLE_COLLISION_EVENT_ID = "CollectibleCollisionEvent";

    public static void addCollider(Collidable collidable) {
        if(collidable.getName().equals(Jumper.NAME)) {
            assert jumper == null;
            jumper = collidable;
        }else {
            collidables.add(collidable);
        }
    }

    public static void clear() {
        collidables.clear();
        jumper = null;
    }

    public static void update(float deltaTime) {
        for(Collidable collidable : collidables) {
            if(jumper.collidesWith(collidable)) {
                CollisionData collisionData = new CollisionData(jumper, collidable);

                String eventId = (collidable.getName().equals(Collectible.ID))
                        ? COLLECTIBLE_COLLISION_EVENT_ID
                        : COLLISION_EVENT_ID;

                NotificationManager.registerEvent(eventId, collisionData);
            }
        }
    }

    public static class CollisionData {
        private final Collidable jumperObject;
        private final Collidable otherObject;

        public CollisionData(Collidable jumperObject, Collidable otherObject) {
            this.jumperObject = jumperObject;
            this.otherObject = otherObject;
        }

        public Collidable getJumperObject() {
            return jumperObject;
        }

        public Collidable getOtherObject() {
            return otherObject;
        }
    }
}
