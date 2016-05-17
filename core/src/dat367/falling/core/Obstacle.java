package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Quad;
import dat367.falling.platform_abstraction.QuadRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Obstacle implements Positioned {

    public static final String ID = "Obstacle";

    private SphereCollider sphereCollider;
    private Vector position;

    private Quad quad;


    public Obstacle(ResourceRequirements resourceRequirements, Vector position) {
        quad = new Quad("balloonfilled.png", true, true, 1000, 100, 1, 1, false);
        resourceRequirements.require(quad);
        sphereCollider = new SphereCollider(this, ID, 10);
        CollisionManager.addCollider(sphereCollider);
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    public void update(float deltaTime){
        RenderQueue.addTask(new QuadRenderTask(quad, getPosition(), new Vector(0, 0, 0), new Vector(10, 1, 10)));
    }
}
