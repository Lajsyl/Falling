package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Quad;
import dat367.falling.platform_abstraction.QuadRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Obstacle implements Positioned {

    public static final String ID = "Obstacle";
    public static final String POSITION_CHANGED_EVENT_ID = "ObstaclePositionChangedEvent";

    private SphereCollider sphereCollider;
    private Vector position;

    private Quad quad;
    private boolean enabled = true;


    public Obstacle(ResourceRequirements resourceRequirements, Vector position) {
        quad = new Quad("mine.png", true, true, 1000, 100, 1, 1, false);
        resourceRequirements.require(quad);
        sphereCollider = new SphereCollider(this, ID, 10);
        CollisionManager.addCollider(sphereCollider);
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    @Override
    public String getPositionChangedEventID() {
        return POSITION_CHANGED_EVENT_ID;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPosition(Vector position) {
        this.position = position;
        NotificationManager.registerEvent(getPositionChangedEventID(), this);
    }

    public void update(float deltaTime){
        if (enabled) {
            RenderQueue.addTask(new QuadRenderTask(quad, getPosition(), new Rotation(), new Vector(10, 1, 10)));
        }
    }
}
