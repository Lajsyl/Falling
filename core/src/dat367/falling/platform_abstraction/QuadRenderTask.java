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

}
