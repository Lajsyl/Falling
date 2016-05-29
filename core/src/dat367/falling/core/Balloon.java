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
    private boolean isSecretBalloon;

    public Balloon(ResourceRequirements resourceRequirements, Vector position, boolean isSecretBalloon){
        model = new Model("balloon.g3db", true, true, BalloonGameMode.BALLOON_MAX_DRAW_DISTANCE, BalloonGameMode.BALLOON_FADE_OUT_DISTANCE);
        resourceRequirements.require(model);
        collider = new SphereCollider(this, INTERACTABLE_ID, 5.0f/3);
        CollisionManager.addCollider(collider);
        this.position = position;
        this.isSecretBalloon = isSecretBalloon;
    }

    public Balloon(ResourceRequirements resourceRequirements, Vector position) {
        this(resourceRequirements, position, false);
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
        //Draw balloon if enabled
        if (enabled) {
            RenderQueue.getDefault().addTask(new ModelRenderTask(model, getPosition(), new Rotation(), new Vector(5.0f/3,5.0f/3,5.0f/3)));
        }
    }

    public boolean isSecretBalloon() {
        return isSecretBalloon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Balloon balloon = (Balloon) o;

        if (isSecretBalloon != balloon.isSecretBalloon) return false;
        return model != null ? model.equals(balloon.model) : balloon.model == null;

    }

    @Override
    public int hashCode() {
        int result = model != null ? model.hashCode() : 0;
        result = 31 * result + (isSecretBalloon ? 1 : 0);
        return result;
    }
}
