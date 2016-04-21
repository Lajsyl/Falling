package dat367.falling.core.world;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Quad;
import dat367.falling.platform_abstraction.QuadRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Cloud {
    private Quad quad;
    private Vector position;
    private Vector velocity;
    private float scale;

    public Cloud(ResourceRequirements resourceRequirements) {
        quad = new Quad("cloud_01.png", true);
        resourceRequirements.require(quad);
    }


    public void update(float deltaTime) {
        position.add(velocity.scale(deltaTime));
        RenderQueue.addTask(new QuadRenderTask(quad, position, new Vector(0, 0, 0), new Vector(scale, scale, scale)));
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
