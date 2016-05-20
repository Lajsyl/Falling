package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Island implements Positioned {

    private Vector position;
    private Model island = new Model("island.g3db");
    private ModelRenderTask renderIsland;
    HeightMap heightMap = new HeightMap("island_heightmap.png");
    HeightMapCollider heightMapCollider;
    public static final String ID = "Island";

    public Island(ResourceRequirements resourceRequirements, Vector position) {
        this.position = position;

        renderIsland = new ModelRenderTask(island, position, new Rotation(), new Vector(10,10,10));
        resourceRequirements.require(island);

        resourceRequirements.require(heightMap);
        heightMapCollider = new HeightMapCollider(this, ID, heightMap, position, 2400, 2400, 200);
        CollisionManager.addCollider(heightMapCollider);

    }

    public void update(float deltaTime) {
        RenderQueue.addTask(renderIsland);
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }
}
