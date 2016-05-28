package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Island implements Positioned {

    private Vector position;
    HeightMap heightMap = new HeightMap("island_heightmap.png");
    HeightMapCollider heightMapCollider;
    public static final String ID = "Island";

    public Island(ResourceRequirements resourceRequirements, Vector position) {
        this.position = position;
        float islandRadius = 500.0f;
        float islandHeight = 206.94476f;
        resourceRequirements.require(heightMap);
        heightMapCollider = new HeightMapCollider(this, ID, heightMap, position, 2*islandRadius, 2*islandRadius, islandHeight);
        CollisionManager.addCollider(heightMapCollider);
    }

    public void update(float deltaTime) {
        // Visual debugging of the heightmap
//        Model blip = new Model("balloon.g3db");
//        for (int z = -1100; z < 1100; z += 35) {
//            for (int x = -1100; x < 1100; x += 35) {
//
//                Vector xzPosition = new Vector(x, 0, z);
//                if (heightMapCollider.pointIsInsideXZBoundary(xzPosition)) {
//
//                    Vector position = new Vector(x, heightMapCollider.getHeight(x, z), z);
//                    RenderTask currentBlip = new ModelRenderTask(
//                            blip,
//                            position,
//                            new Rotation(),
//                            new Vector(2f, 2f, 2f)
//                    );
//                    RenderQueue.getDefault().addTask(currentBlip);
//                }
//
//            }
//        }
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
