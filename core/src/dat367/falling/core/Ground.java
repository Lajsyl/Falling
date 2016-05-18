package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Ground {

    private Quad groundQuad;
    private QuadRenderTask renderGround;

    private Model island = new Model("island.g3db");
    private ModelRenderTask renderIsland;

    public static final float SCALE = 30000.0f;

    public static final String TEXTURE_NAME = "waterclear256blue.bmp";
    public static final float ESTIMATED_TEXTURE_SIZE = 1200.0f; /* meters */
    public static final float UV_SCALE = SCALE / ESTIMATED_TEXTURE_SIZE;



    public Ground(ResourceRequirements resourceRequirements) {
        this.groundQuad = new Quad(TEXTURE_NAME, true, true, SCALE, 15000, UV_SCALE, UV_SCALE, false);
        resourceRequirements.require(groundQuad);

        renderIsland = new ModelRenderTask(island, new Vector(0,0,0), new Rotation(), new Vector(10,10,10));
        resourceRequirements.require(island);

        renderGround = new QuadRenderTask(
                this.groundQuad,
                new Vector(0, 0, 0),
                new Rotation(),
                new Vector(SCALE, 1, SCALE)
        );
    }

    public void update(float deltaTime) {
        RenderQueue.addTask(renderGround);
        RenderQueue.addTask(renderIsland);
    }
}
