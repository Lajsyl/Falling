package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Collectible implements Positioned {

    //TODO: Make this abstract so that different collectibles can be added
    public static final String ID = "Collectible";

    private SphereCollider sphereCollider;
    private Vector position;

    private Model model;

    private boolean enabled = true;

    public Collectible(ResourceRequirements resourceRequirements, Vector position){
        model = new Model("balloon.g3db");
        resourceRequirements.require(model);
        sphereCollider = new SphereCollider(this, ID, 5);
        CollisionManager.addCollider(sphereCollider);
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void update(float deltaTime) {
        if (enabled) {
            RenderQueue.addTask(new ModelRenderTask(model, getPosition(), new Rotation(), new Vector(5,5,5)));
        }
    }
}
