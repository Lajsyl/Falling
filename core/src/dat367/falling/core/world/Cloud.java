package dat367.falling.core.world;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Cloud {
    private Quad quad;
    private Vector position = new Vector(0, 0, 0);
    private Vector velocity = new Vector(0, 0, 0);
    private float scale = 1.0f;

    public Cloud(ResourceRequirements resourceRequirements) {
        quad = new Quad("cloud_01.png");
        resourceRequirements.require(quad);
    }


    public void update(float deltaTime) {
        this.position = position.add(velocity.scale(deltaTime));

        RenderTask cloudTask = new QuadRenderTask(quad, position, new Vector(0,0,0), new Vector(scale,1,scale));
        RenderQueue.addTask(cloudTask);
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
