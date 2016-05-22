package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Obstacle implements Positioned {


    //TODO: Make this abstract so that different obstacles can be added

    public static final String ID = "Obstacle";
    public static final String POSITION_CHANGED_EVENT_ID = "ObstaclePositionChangedEvent";

    private SphereCollider sphereCollider;
    private Vector position;

    private Model model;
    private boolean enabled = true;

    private float explosiveness;

    public Obstacle(ResourceRequirements resourceRequirements, Vector position, float explosiveness) {
        model = new Model("mine.g3db");
        resourceRequirements.require(model);
        sphereCollider = new SphereCollider(this, ID, 10);
        CollisionManager.addCollider(sphereCollider);
        this.position = position;
        this.explosiveness = explosiveness;
    }

    public Obstacle(ResourceRequirements resourceRequirements, Vector position) {
        this(resourceRequirements, position, 1.0f);
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

    public float getExplosiveness() {
        return explosiveness;
    }

    public void update(float deltaTime){
        if (enabled) {
            RenderQueue.addTask(new ModelRenderTask(model, getPosition(), new Rotation(), new Vector(5,5,5)));
        }
    }
}
