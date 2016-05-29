package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Model;
import dat367.falling.platform_abstraction.ModelRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Mine extends Interactable {


    public static final String INTERACTABLE_ID = "Mine";
    public static final String POSITION_CHANGED_EVENT_ID = "ObstaclePositionChangedEvent";

    private Model model;
    private float explosiveness;

    public Mine(ResourceRequirements resourceRequirements, Vector position, float explosiveness) {
        model = new Model("mine.g3db", true, true, BalloonGameMode.BALLOON_MAX_DRAW_DISTANCE, BalloonGameMode.BALLOON_FADE_OUT_DISTANCE);
        resourceRequirements.require(model);
        collider = new SphereCollider(this, INTERACTABLE_ID, 5.0f/3);
        CollisionManager.addCollider(collider);
        this.position = position;
        this.explosiveness = explosiveness;
    }

    public Mine(ResourceRequirements resourceRequirements, Vector position) {
        this(resourceRequirements, position, 1.0f);
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
            RenderQueue.getDefault().addTask(new ModelRenderTask(model, getPosition(), new Rotation(), new Vector(5.0f/3,5.0f/3,5.0f/3)));
        }
    }

    public float getExplosiveness() {
        return explosiveness;
    }

    public void setExplosiveness(float explosiveness) {
        this.explosiveness = explosiveness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mine mine = (Mine) o;

        if (Float.compare(mine.explosiveness, explosiveness) != 0) return false;
        return model != null ? model.equals(mine.model) : mine.model == null;

    }

    @Override
    public int hashCode() {
        int result = model != null ? model.hashCode() : 0;
        result = 31 * result + (explosiveness != +0.0f ? Float.floatToIntBits(explosiveness) : 0);
        return result;
    }
}


