package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Collectible implements Positioned {

    public static final String ID = "Collectible";

    private SphereCollider sphereCollider;
    private Vector position;

    private Model model;


    public Collectible(ResourceRequirements resourceRequirements, Vector position){
        model = new Model("balloon.g3db");
        resourceRequirements.require(model);
        sphereCollider = new SphereCollider(this, ID, 10);
        CollisionManager.addCollider(sphereCollider);
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    public void update(float deltaTime) {
        RenderQueue.addTask(new ModelRenderTask(model, getPosition(), new Rotation(), new Vector(10,10,10)));
    }
}
