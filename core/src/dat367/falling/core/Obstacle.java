package dat367.falling.core;

import dat367.falling.math.Rotation;
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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void update(float deltaTime){
        if (enabled) {
            RenderQueue.addTask(new QuadRenderTask(quad, getPosition(), new Rotation(), new Vector(10, 1, 10)));
        }
    }
}
