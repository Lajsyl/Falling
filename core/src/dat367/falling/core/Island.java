package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.HeightMap;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Island implements Positioned {

    HeightMap heightMap = new HeightMap("island_heightmap.png");
    HeightMapCollider heightMapCollider;
    public static final String ID = "Island";

    public Island(ResourceRequirements resourceRequirements) {
        resourceRequirements.require(heightMap);
//        heightMapCollider = new HeightMapCollider(this, ID, "island_heightmap.bmp");
    }

    @Override
    public Vector getPosition() {
        return null;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }
}
