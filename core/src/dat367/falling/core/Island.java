package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Island implements Positioned {

    private Vector position;
//    private Model island = new Model("island.g3db");
//    private ModelRenderTask renderIsland;
    HeightMap heightMap = new HeightMap("island_heightmap.png");
    HeightMapCollider heightMapCollider;
    public static final String ID = "Island";

    private Model blip = new Model("balloon.g3db");

    public Island(ResourceRequirements resourceRequirements, Vector position) {
        this.position = position;

        float islandModelHeight = 0.617f;

        float islandRadius = 500.0f;
        float islandHeight = 200.0f;//islandRadius*islandModelHeight;
        float islandHeightScale = islandHeight / islandModelHeight;
//        renderIsland = new ModelRenderTask(island, position, new Rotation(), new Vector(islandRadius,islandHeightScale,islandRadius));
//        resourceRequirements.require(island);

        resourceRequirements.require(heightMap);
        heightMapCollider = new HeightMapCollider(this, ID, heightMap, position, 2*islandRadius, 2*islandRadius, islandHeight);
        CollisionManager.addCollider(heightMapCollider);
    }

    public void update(float deltaTime) {
//        RenderQueue.getDefault().addTask(renderIsland);

        for (int z = -1100; z < 1100; z += 35) {
            for (int x = -1100; x < 1100; x += 35) {

                Vector xzPosition = new Vector(x, 0, z);
                if (heightMapCollider.pointIsInsideXZBoundary(xzPosition)) {

                    Vector position = new Vector(x, heightMapCollider.getHeight(x, z), z);
                    RenderTask currentBlip = new ModelRenderTask(
                            blip,
                            position,
                            new Rotation(),
                            new Vector(1f, 1f, 1f)
                    );
                    RenderQueue.getDefault().addTask(currentBlip);
                }

            }
        }
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
