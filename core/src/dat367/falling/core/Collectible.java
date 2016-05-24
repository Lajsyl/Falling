package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Model;
import dat367.falling.platform_abstraction.ModelRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Collectible implements Positioned {

    //TODO: Make this abstract so that different collectibles can be added
    public static final String ID = "Collectible";
    public static final String POSITION_CHANGED_EVENT_ID = "CollectiblePositionChangedEvent";

    private SphereCollider sphereCollider;
    private Vector position;

    private Model model;

    private boolean enabled = true;

    public Collectible(ResourceRequirements resourceRequirements, Vector position){
        model = new Model("balloon.g3db", true, true, BalloonGameMode.BALLOON_MAX_DRAW_DISTANCE, BalloonGameMode.BALLOON_FADE_OUT_DISTANCE);
        resourceRequirements.require(model);
        sphereCollider = new SphereCollider(this, ID, 5);
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

    public void update(float deltaTime) {
        if (enabled) {
            RenderQueue.addTask(new ModelRenderTask(model, getPosition(), new Rotation(), new Vector(5,5,5)));
        }
    }
}
