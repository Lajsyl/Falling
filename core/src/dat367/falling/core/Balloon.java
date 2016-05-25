package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Model;
import dat367.falling.platform_abstraction.ModelRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Balloon extends Interactable {

    public static final String INTERACTABLE_ID = "Balloon";
    public static final String POSITION_CHANGED_EVENT_ID = "CollectiblePositionChangedEvent";

    private Model model;

    private boolean enabled = true;

    public Balloon(ResourceRequirements resourceRequirements, Vector position){
        model = new Model("balloon.g3db", true, true, BalloonGameMode.BALLOON_MAX_DRAW_DISTANCE, BalloonGameMode.BALLOON_FADE_OUT_DISTANCE);
        resourceRequirements.require(model);
        collider = new SphereCollider(this, INTERACTABLE_ID, 5);
        CollisionManager.addCollider(collider);
        this.position = position;
    }

    @Override
    public String getPositionChangedEventID() {
        return POSITION_CHANGED_EVENT_ID;
    }

    @Override
    public String getInteractableID() {
        return INTERACTABLE_ID;
    }

    public void update(float deltaTime){
        if (enabled) {
            RenderQueue.addTask(new ModelRenderTask(model, getPosition(), new Rotation(), new Vector(5,5,5)));
        }
    }
}
