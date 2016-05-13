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

    public static final int MAX_NUMBER_OF_CLOUDS = 40;
    public static final float WIND_DIRECTION_DEVIATION_SCALE = 0.25f;

    private LinkedList<Cloud> activeClouds = new LinkedList<Cloud>();
    private LinkedList<Cloud> passiveClouds = new LinkedList<Cloud>();

    private CloudSimulationConfig simulationConfig;
    private Vector defaultWindDirection;
    private Vector basePosition;

    private Vector airplaneVelocity;

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

    public void update(float deltaTime, Jumper jumper) {
        this.basePosition = jumper.getPosition();
        int recommendedCloudCount = simulationConfig.getCloudAmountForHeight(getJumperHeight(), MAX_NUMBER_OF_CLOUDS);

        // Get additional velocity from the airplane
        // TODO: Implement properly!
        if (airplaneVelocity == null) {
            final float airplaneSpeed = 95.0f;
            airplaneVelocity = new Vector(0, 0, 1).normalized().scale(airplaneSpeed);
        }
        else if (!(jumper.getFallState() instanceof PreJumpState)) {
            airplaneVelocity = airplaneVelocity.scale(0.997f);
        }
        Vector cloudAirplaneVelocity = airplaneVelocity.scale(-1);

        // Update and "distribute" available clouds
        for (Iterator<Cloud> cloudIterator = activeClouds.iterator(); cloudIterator.hasNext(); /*_*/) {
            Cloud cloud = cloudIterator.next();

            if (cloudIsOutsideBounds(cloud)) {
                // If there are more active clouds that recommended, put it in the passive list
                if (activeClouds.size() > recommendedCloudCount) {
                    passiveClouds.add(cloud);
                    cloudIterator.remove();
                } else {
                    if (jumper.getFallState() instanceof PreJumpState) {
                        // TODO: Make sure clouds only spawn in the leading edge of the airplane direction, now it's possible that it spawn on the left side, and since the place goes right in high speed we will never see them.
                        //spawnCloudAtSpawnBorders(cloud);
                        spawnCloudAtBottom(cloud);
                        randomizeYPosition(cloud);
                    } else {
                        // TODO: Make sure there isn't a big blob of clouds when you encounter the first clouds spawned at the bottom (i.e. when fall state initially is changed to FreeFallingState here below)
                        spawnCloudAtBottom(cloud);
                    }
                }
            }

            cloud.update(deltaTime, cloudAirplaneVelocity);
        }

        // Spawn extra clouds if needed
        while (activeClouds.size() < recommendedCloudCount) {
            activeClouds.add(passiveClouds.remove());
        }
    }

    private void spawnCloudAtBottom(Cloud cloud) {
        randomizePosition(cloud);
        randomizeVelocity(cloud);
        randomizeScale(cloud);
    }
    
    private void spawnCloudAtSpawnBorders(Cloud cloud) {
        randomizeSpawnBorderPosition(cloud);
        randomizeYPosition(cloud);
        randomizeVelocity(cloud);
        randomizeScale(cloud);
    }

    private void randomizeSpawnBorderPosition(Cloud cloud) {
        float distanceAlongBorder = (random.nextFloat() - 0.5f) * 2 * CLOUD_SPAWN_AREA_RADIUS * 2;
        Vector spawnAreaCorner = basePosition.sub(new Vector(CLOUD_SPAWN_AREA_RADIUS, 0, CLOUD_SPAWN_AREA_RADIUS));
        Vector newPosition = random.nextBoolean() ? spawnAreaCorner.add(new Vector(distanceAlongBorder, 0, 0))
                                                  : spawnAreaCorner.add(new Vector(0, 0, distanceAlongBorder));

        cloud.setPosition(newPosition);
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


}
