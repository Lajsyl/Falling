package dat367.falling.core.world;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Quad;
import dat367.falling.platform_abstraction.QuadRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Ground {

    private Quad groundQuad;
    private QuadRenderTask renderGround;

    public static final float GROUND_SCALE = 7000.0f;

    public Ground(ResourceRequirements resourceRequirements) {
        this.groundQuad = new Quad("ground_test.jpg", true, true);
        resourceRequirements.require(groundQuad);

        renderGround = new QuadRenderTask(
                this.groundQuad,
                new Vector(0, 0, 0),
                new Vector(0, 0, 0),
                new Vector(GROUND_SCALE, 1, GROUND_SCALE)
        );
    }

    public void update(float deltaTime) {
        RenderQueue.addTask(renderGround);
    }
}
