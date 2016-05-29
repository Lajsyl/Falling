package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class CloudSimulator {

    interface CloudSimulationConfig {
        int getCloudAmountForHeight(float height, int maxAmountOfClouds);
        float getWindSpeedForHeight(float height);
    }

    public static final float SIMULATION_SCALE = 0.35f;
    public static final float CLOUD_MIN_SIZE = 75.0f * SIMULATION_SCALE;
    public static final float CLOUD_MAX_SIZE = 800.0f * SIMULATION_SCALE;

    public static final float CLOUD_SPAWN_AREA_HEIGHT = 1000.0f;
    public static final float CLOUD_SPAWN_AREA_RADIUS = CLOUD_SPAWN_AREA_HEIGHT / 2;
    public static final float CLOUD_DESPAWN_BOUNDS_EXTENSION = 500.0f;

    public static final int MAX_NUMBER_OF_CLOUDS = 20;
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
        for (int i = 0; i < currentCloudCount; i++) {
            // Get cloud from passive clouds
            Cloud cloud = passiveClouds.remove();

            spawnCloudAtBottom(cloud);
            randomizeYPosition(cloud);

            // Put cloud in list of active clouds
            activeClouds.add(cloud);
        }
    }

    private boolean cloudIsOutsideBounds(Cloud cloud) {
        // Is cloud above "roof"?
        float currentRoof = basePosition.getY() + CLOUD_SPAWN_AREA_HEIGHT / 2;
        if (cloud.getPosition().getY() > currentRoof) {
            return true;
        }

        // Is cloud outside radius?
        final float distanceToCloudCenter = cloud.getPosition().sub(basePosition).length();
        final float distanceToCloudEdge = distanceToCloudCenter - cloud.getScale() / 2;
        return distanceToCloudEdge > CLOUD_SPAWN_AREA_RADIUS + CLOUD_DESPAWN_BOUNDS_EXTENSION;
    }

    public void update(float deltaTime, Jumper jumper, Airplane airplane) {
        this.basePosition = jumper.getPosition();
        int recommendedCloudCount = simulationConfig.getCloudAmountForHeight(getJumperHeight(), MAX_NUMBER_OF_CLOUDS);

        // Get additional velocity from the airplane
        Vector additionalCloudVelocity = Airplane.VELOCITY.sub(airplane.getActualVelocity()).scale(-1.0f);

        // Update and "distribute" available clouds
        for (Iterator<Cloud> cloudIterator = activeClouds.iterator(); cloudIterator.hasNext(); /*_*/) {
            Cloud cloud = cloudIterator.next();

            if (cloudIsOutsideBounds(cloud)) {
                // If there are more active clouds that recommended, put it in the passive list
                if (activeClouds.size() > recommendedCloudCount) {
                    passiveClouds.add(cloud);
                    cloudIterator.remove();
                } else {
                    spawnCloud(cloud, jumper.getFallState(), airplane);
                }
            }
            cloud.update(deltaTime, additionalCloudVelocity);
        }

        // Spawn extra clouds if needed
        while (activeClouds.size() < recommendedCloudCount) {
            activeClouds.add(passiveClouds.remove());
        }
    }

    private void spawnCloud(Cloud cloud, FallState fallState, Airplane airplane) {
        if (fallState instanceof PreJumpState) {
            // If the jumper is still in the plane, always spawn on the leading edge.
            spawnCloudAtLeadingEdge(cloud);
        } else {
            // If the jumper has left the airplane spawn at bottom of spawn volume. However, to make the segue smooth
            // successively move over to spawning at the bottom.

            float actualSpeed = airplane.getActualVelocity().length();
            float maxSpeed = Airplane.VELOCITY.length();
            float bottomSpawningRatio = actualSpeed / maxSpeed;

            // Offset it a bit towards spawning at the leading edge.
            bottomSpawningRatio = (float) Math.pow(bottomSpawningRatio, 2.0);

            if (random.nextFloat() < bottomSpawningRatio) {
                spawnCloudAtBottom(cloud);
            } else {
                spawnCloudAtLeadingEdge(cloud);
            }
        }
    }

    private void spawnCloudAtBottom(Cloud cloud) {
        randomizePosition(cloud);
        randomizeVelocity(cloud);
        randomizeScale(cloud);
    }
    
    private void spawnCloudAtLeadingEdge(Cloud cloud) {
        randomizeSpawnBorderPosition(cloud);
        randomizeYPosition(cloud);
        randomizeVelocity(cloud);
        randomizeScale(cloud);
    }

    private void randomizeSpawnBorderPosition(Cloud cloud) {
        Vector airplaneDirection = Airplane.VELOCITY.normalized();
        Vector airplaneRight = new Vector(0, 1, 0).cross(airplaneDirection);

        float leftOrRight = random.nextBoolean() ? +1.0f : -1.0f;
        float randomDeviation = random.nextFloat() * CLOUD_SPAWN_AREA_RADIUS * leftOrRight;

        Vector leadingSpawnEdge = airplaneDirection.scale(CLOUD_SPAWN_AREA_RADIUS);
        Vector leadingEdgeDeviation = airplaneRight.scale(randomDeviation);

        Vector spawnPosition = basePosition.add(leadingSpawnEdge).add(leadingEdgeDeviation);

        cloud.setPosition(spawnPosition);
    }

    private void randomizePosition(Cloud cloud) {
        float x = basePosition.getX() + (random.nextFloat() - 0.5f) * 2 * CLOUD_SPAWN_AREA_RADIUS;
        float z = basePosition.getZ() + (random.nextFloat() - 0.5f) * 2 * CLOUD_SPAWN_AREA_RADIUS;
        float y = basePosition.getY() - CLOUD_SPAWN_AREA_HEIGHT / 2;

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
        float y = (basePosition.getY() - CLOUD_SPAWN_AREA_HEIGHT / 2) + random.nextFloat() * CLOUD_SPAWN_AREA_HEIGHT;
        cloud.setPosition(new Vector(cloud.getPosition().getX(), y, cloud.getPosition().getZ()));
    }

    private Vector getRandomWindDirection() {
        return new Vector(random.nextFloat() - 0.5f, 0, random.nextFloat() - 0.5f).normalized();
    }

    private float getJumperHeight() {
        return basePosition.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CloudSimulator that = (CloudSimulator) o;

        if (activeClouds != null ? !activeClouds.equals(that.activeClouds) : that.activeClouds != null) return false;
        if (passiveClouds != null ? !passiveClouds.equals(that.passiveClouds) : that.passiveClouds != null)
            return false;
        if (simulationConfig != null ? !simulationConfig.equals(that.simulationConfig) : that.simulationConfig != null)
            return false;
        if (defaultWindDirection != null ? !defaultWindDirection.equals(that.defaultWindDirection) : that.defaultWindDirection != null)
            return false;
        if (basePosition != null ? !basePosition.equals(that.basePosition) : that.basePosition != null) return false;
        return random != null ? random.equals(that.random) : that.random == null;

    }

    @Override
    public int hashCode() {
        int result = activeClouds != null ? activeClouds.hashCode() : 0;
        result = 31 * result + (passiveClouds != null ? passiveClouds.hashCode() : 0);
        result = 31 * result + (simulationConfig != null ? simulationConfig.hashCode() : 0);
        result = 31 * result + (defaultWindDirection != null ? defaultWindDirection.hashCode() : 0);
        result = 31 * result + (basePosition != null ? basePosition.hashCode() : 0);
        result = 31 * result + (random != null ? random.hashCode() : 0);
        return result;
    }
}
