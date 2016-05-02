package dat367.falling.core.world;

import dat367.falling.core.Jumper;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class CloudSimulator {

    public static final float SIMULATION_SCALE = 0.35f;
    public static final float RADIUS = 1200 * SIMULATION_SCALE;

    public static final float CLOUD_MIN_SIZE = 75.0f * SIMULATION_SCALE;
    public static final float CLOUD_MAX_SIZE = 800.0f * SIMULATION_SCALE;

    public static final float HEIGHT_ABOVE = 300;
    public static final float HEIGHT_BELOW = 500;

    public static final int MAX_NUMBER_OF_CLOUDS = 50;
    public static final float WIND_DIRECTION_DEVIATION_SCALE = 0.25f;

    private LinkedList<Cloud> activeClouds = new LinkedList<Cloud>();
    private LinkedList<Cloud> passiveClouds = new LinkedList<Cloud>();

    private CloudSimulationConfig simulationConfig;
    private Vector defaultWindDirection;
    private Vector basePosition;

    private Random random = new Random();


    public CloudSimulator(ResourceRequirements resourceRequirements, Jumper jumper) {
        this.basePosition = jumper.getPosition();
        this.defaultWindDirection = getRandomWindDirection();

        // Assume that the jumper spawns at the highest position it will ever reach
        this.simulationConfig = new DefaultCloudSimulationConfig(getJumperHeight());

        for (int i = 0; i < MAX_NUMBER_OF_CLOUDS; i++) {
            Cloud cloud = new Cloud(resourceRequirements);
            passiveClouds.add(cloud);
        }

        float currentCloudCount = simulationConfig.getCloudAmountForHeight(getJumperHeight(), MAX_NUMBER_OF_CLOUDS);
        assert currentCloudCount < MAX_NUMBER_OF_CLOUDS;
        for (int i = 0; i < currentCloudCount; i++) {
            // Get cloud from passive clouds
            Cloud cloud = passiveClouds.remove();

            spawnCloudAtBottom(cloud);
            randomizeYPosition(cloud);

            // Put cloud in list of active clouds
            activeClouds.add(cloud);
        }
    }

    public void update(float deltaTime, Jumper jumper) {
        this.basePosition = jumper.getPosition();
        float currentRoof = basePosition.getY() + HEIGHT_ABOVE;

        Iterator<Cloud> cloudIterator = activeClouds.iterator();
        while (cloudIterator.hasNext()) {
            Cloud cloud = cloudIterator.next();
            if (cloud.getPosition().getY() > currentRoof) {
                float recommendedCloudCount = simulationConfig.getCloudAmountForHeight(getJumperHeight(), MAX_NUMBER_OF_CLOUDS);

                // If there are more active clouds that recommended, put it in the passive list
                if (activeClouds.size() > recommendedCloudCount) {
                    passiveClouds.add(cloud);
                    cloudIterator.remove();
                } else {
                    spawnCloudAtBottom(cloud);
                }
            }
            cloud.update(deltaTime);
        }
    }

    private void spawnCloudAtBottom(Cloud cloud) {
        randomizePosition(cloud);
        randomizeVelocity(cloud);
        randomizeScale(cloud);
    }

    private void randomizePosition(Cloud cloud) {
        float x = basePosition.getX() + (random.nextFloat() - 0.5f) * 2 * RADIUS;
        float z = basePosition.getZ() + (random.nextFloat() - 0.5f) * 2 * RADIUS;
        float y = basePosition.getY() - HEIGHT_BELOW;

        cloud.setPosition(new Vector(x, y, z));
    }

    private void randomizeVelocity(Cloud cloud) {
        float windSpeed = simulationConfig.getWindSpeedForHeight(getJumperHeight());

        Vector windDirectionDeviation = getRandomWindDirection().scale(WIND_DIRECTION_DEVIATION_SCALE);
        Vector velocity = defaultWindDirection.add(windDirectionDeviation).normalized().scale(windSpeed);
        cloud.setVelocity(velocity);
    }

    private void randomizeScale(Cloud cloud) {
        final float maxDifference = CLOUD_MAX_SIZE - CLOUD_MIN_SIZE;
        cloud.setScale(CLOUD_MIN_SIZE + random.nextFloat() * maxDifference);
    }

    private void randomizeYPosition(Cloud cloud) {
        float y = basePosition.getY() - HEIGHT_BELOW + random.nextFloat() * (HEIGHT_BELOW + HEIGHT_ABOVE);
        cloud.setPosition(new Vector(cloud.getPosition().getX(), y, cloud.getPosition().getZ()));
    }

    private Vector getRandomWindDirection() {
        return new Vector(random.nextFloat(), 0, random.nextFloat()).normalized();
    }

    private float getJumperHeight() {
        return basePosition.getY();
    }

    interface CloudSimulationConfig {
        float getCloudAmountForHeight(float height, int maxAmountOfClouds);
        float getWindSpeedForHeight(float height);
    }


}
