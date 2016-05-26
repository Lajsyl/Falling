package dat367.falling.core;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    private static List<Collider> colliders = new ArrayList<Collider>();
    private static Collider jumperCollider;

    public static final String COLLISION_EVENT_ID = "CollisionEvent";
    public static final String COLLECTIBLE_COLLISION_EVENT_ID = "CollectibleCollisionEvent";
    public static final String OBSTACLE_COLLISION_EVENT_ID = "ObstacleCollisionEvent";
    public static final String ISLAND_COLLISION_EVENT_ID = "IslandCollisionEvent";

    public static void addCollider(Collider collider) {
        if(collider.getName().equals(Jumper.NAME)) {
            assert jumperCollider == null;
            jumperCollider = collider;
        }else {
            colliders.add(collider);
        }
    }

    public static void clear() {
        colliders.clear();
        jumperCollider = null;
    }

    public static void update(float deltaTime) {
        for(Collider collider : colliders) {
            if(jumperCollider.collidesWith(collider)) {
                CollisionData collisionData = new CollisionData(jumperCollider, collider);

                String eventId = getEventID(collider);
                NotificationManager.getDefault().registerEvent(eventId, collisionData);
            }
        }
    }

    private static String getEventID(Collider collider) {
        if (collider.getName().equals(Balloon.INTERACTABLE_ID)) return COLLECTIBLE_COLLISION_EVENT_ID;
        if (collider.getName().equals(Mine.INTERACTABLE_ID)) return OBSTACLE_COLLISION_EVENT_ID;
        if (collider.getName().equals(Island.ID)) return ISLAND_COLLISION_EVENT_ID;

        // Generic collision event
        return COLLISION_EVENT_ID;
    }

    public static class CollisionData {
        private final Collider jumperObject;
        private final Collider otherObject;

        public CollisionData(Collider jumperObject, Collider otherObject) {
            this.jumperObject = jumperObject;
            this.otherObject = otherObject;
        }

        public Collider getJumperObject() {
            return jumperObject;
        }

        public Collider getOtherObject() {
            return otherObject;
        }
    }
}
