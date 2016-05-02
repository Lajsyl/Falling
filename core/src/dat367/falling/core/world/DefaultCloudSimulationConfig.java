package dat367.falling.core.world;

import dat367.falling.math.FallingMath;

import java.util.Random;

public class DefaultCloudSimulationConfig implements CloudSimulator.CloudSimulationConfig {

    private Random random = new Random();
    private float maxHeight;

    public DefaultCloudSimulationConfig(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public float getCloudAmountForHeight(float height, int maxAmountOfClouds) {
        // Only clouds in height 1000m-4000m, and most at 4000.
        float multiple = FallingMath.smoothstep(4000, 1000, height);
        return (1.0f - multiple) * maxAmountOfClouds;
    }

    @Override
    public float getWindSpeedForHeight(float height) {
        return (height / maxHeight) * 25.0f;
    }
}
