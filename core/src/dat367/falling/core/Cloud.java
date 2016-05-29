package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Cloud {

    private Quad quad;
    private Vector position = new Vector(0, 0, 0);
    private Vector velocity = new Vector(0, 0, 0);
    private float scale = 1.0f;

    public Cloud(ResourceRequirements resourceRequirements) {
        quad = new Quad("cloud_01.png", true, true, CloudSimulator.CLOUD_SPAWN_AREA_HEIGHT / 2, 250, 1, 1, false);
        resourceRequirements.require(quad);
    }
    
    public void update(float deltaTime, Vector additionalVelocity) {
        Vector additional = additionalVelocity.scale(deltaTime);
        this.position = position.add(velocity.scale(deltaTime)).add(additional);

        RenderTask cloudTask = new QuadRenderTask(quad, position, new Rotation(), new Vector(scale,1,scale));
        RenderQueue.getDefault().addTask(cloudTask);
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

    public float getScale() {
        return scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cloud cloud = (Cloud) o;

        if (Float.compare(cloud.scale, scale) != 0) return false;
        if (quad != null ? !quad.equals(cloud.quad) : cloud.quad != null) return false;
        if (position != null ? !position.equals(cloud.position) : cloud.position != null) return false;
        return velocity != null ? velocity.equals(cloud.velocity) : cloud.velocity == null;

    }

    @Override
    public int hashCode() {
        int result = quad != null ? quad.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (velocity != null ? velocity.hashCode() : 0);
        result = 31 * result + (scale != +0.0f ? Float.floatToIntBits(scale) : 0);
        return result;
    }
}
