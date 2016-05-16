package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Quad;
import dat367.falling.platform_abstraction.QuadRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Ground {

    private Quad groundQuad;
    private QuadRenderTask renderGround;

    public static final float SCALE = 70000.0f;

    public static final String TEXTURE_NAME = "waterclear256.bmp";
    public static final float ESTIMATED_TEXTURE_SIZE = 1000.0f; /* meters */
    public static final float UV_SCALE = SCALE / ESTIMATED_TEXTURE_SIZE;

    public Ground(ResourceRequirements resourceRequirements) {
        this.groundQuad = new Quad(TEXTURE_NAME, true, true, SCALE, 10000, UV_SCALE, UV_SCALE, true);
        resourceRequirements.require(groundQuad);

        renderGround = new QuadRenderTask(
                this.groundQuad,
                new Vector(0, 0, 0),
                new Vector(0, 0, 0),
                new Vector(SCALE, 1, SCALE)
        );
    }

    public void update(float deltaTime) {
        RenderQueue.addTask(renderGround);
    }
}
