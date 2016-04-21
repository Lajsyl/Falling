package dat367.falling.platform_abstraction;

import dat367.falling.math.Vector;

public class QuadRenderTask extends RenderTask {

    private final Quad quad;

    public QuadRenderTask(Quad quad, Vector position, Vector orientation, Vector scale) {
        super(position, orientation, scale);
        this.quad = quad;
    }

    public Quad getQuad() {
        return quad;
    }

}
