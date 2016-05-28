package dat367.falling.core;

import dat367.falling.math.FallingMath;

import java.util.Random;

public class DefaultCloudSimulationConfig implements CloudSimulator.CloudSimulationConfig {

    private Random random = new Random();
    private float maxHeight;

    public DefaultCloudSimulationConfig(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public int getCloudAmountForHeight(float height, int maxAmountOfClouds) {
        if (height > 3650) {
            // Render very few clouds around plane (=3700m) for better initial frame rate
            float multiple = FallingMath.smoothstep(3750, 3650, height);
            return Math.round((multiple) * maxAmountOfClouds);
        } else {
            // Only clouds in height 1000m-3650m, and most at 3650.
            float multiple = FallingMath.smoothstep(3650, 1000, height);
            return Math.round((1.0f - multiple) * maxAmountOfClouds);
        }
    }

    @Override
    public float getWindSpeedForHeight(float height) {
        return (height / maxHeight) * 15.0f;
    }
}
