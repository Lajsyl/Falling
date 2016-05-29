package dat367.falling.platform_abstraction;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;

public class QuadRenderTask extends RenderTask {

    private final Quad quad;

    public QuadRenderTask(Quad quad, Vector position, Rotation rotation, Vector scale) {
        super(position, rotation, scale);
        this.quad = quad;
    }

    public Quad getQuad() {
        return quad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuadRenderTask that = (QuadRenderTask) o;

        return quad != null ? quad.equals(that.quad) : that.quad == null;

    }

    @Override
    public int hashCode() {
        return quad != null ? quad.hashCode() : 0;
    }
}
