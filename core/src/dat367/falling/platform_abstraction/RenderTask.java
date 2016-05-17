package dat367.falling.platform_abstraction;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;

public abstract class RenderTask {

    private Vector position;
    private Rotation rotation;
    private Vector scale;

    public RenderTask(Vector position, Rotation rotation, Vector scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector getPosition() {
        return position;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Vector getScale() {
        return scale;
    }

}
