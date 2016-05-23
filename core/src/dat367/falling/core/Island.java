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

        float islandModelHeight = 0.617f;

        float islandRadius = 500.0f;
        float islandHeight = 200.0f;//islandRadius*islandModelHeight;
        float islandHeightScale = islandHeight / islandModelHeight;
        renderIsland = new ModelRenderTask(island, position, new Rotation(), new Vector(islandRadius,islandHeightScale,islandRadius));
        resourceRequirements.require(island);

        resourceRequirements.require(heightMap);
        heightMapCollider = new HeightMapCollider(this, ID, heightMap, position, 2*islandRadius, 2*islandRadius, islandHeight);
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
    public String getPositionChangedEventID() {
        return null;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }
}
