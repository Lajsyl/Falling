package dat367.falling.core;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    private static List<Collidable> collidables = new ArrayList<Collidable>();
    private static Collidable jumper;

    public static final String COLLISION_EVENT_ID = "CollisionEvent";
    public static final String COLLECTIBLE_COLLISION_EVENT_ID = "CollectibleCollisionEvent";
    public static final String OBSTACLE_COLLISION_EVENT_ID = "ObstacleCollisionEvent";

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

                String eventId = getEventID(collidable);
                NotificationManager.registerEvent(eventId, collisionData);
            }
        }
    }

    private static String getEventID(Collidable collidable) {
        if (collidable.getName().equals(Collectible.ID)) return COLLECTIBLE_COLLISION_EVENT_ID;
        if (collidable.getName().equals(Obstacle.ID)) return OBSTACLE_COLLISION_EVENT_ID;

        // Generic collision event
        return COLLISION_EVENT_ID;
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
