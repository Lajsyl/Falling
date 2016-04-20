package dat367.falling.platform_abstraction;

import dat367.falling.math.Vector;

public abstract class RenderTask {

    private Vector position;
    private Vector orientation;
    private Vector scale;

    public RenderTask(Vector position, Vector orientation, Vector scale) {
        this.position = position;
        this.orientation = orientation;
        this.scale = scale;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getOrientation() {
        return orientation;
    }

    public Vector getScale() {
        return scale;
    }

}
