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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenderTask that = (RenderTask) o;

        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        if (rotation != null ? !rotation.equals(that.rotation) : that.rotation != null) return false;
        return scale != null ? scale.equals(that.scale) : that.scale == null;

    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (rotation != null ? rotation.hashCode() : 0);
        result = 31 * result + (scale != null ? scale.hashCode() : 0);
        return result;
    }
}
