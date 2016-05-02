package dat367.falling.core.world;

import dat367.falling.core.Jumper;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudSimulator {
    private List<Cloud> clouds = new ArrayList<Cloud>();
    private Vector basePosition;
    public static float SCALE = 0.35f;
    public static float HEIGHT_ABOVE = 300 * SCALE;
    public static float HEIGHT_BELOW = 500 * SCALE;
    public static float RADIUS = 600 * SCALE;
    private Random random = new Random();

    public CloudSimulator(int numberOfClouds, ResourceRequirements resourceRequirements, Jumper jumper) {
        basePosition = jumper.getPosition();
        for (int i = 0; i < numberOfClouds; i++) {
            Cloud cloud = new Cloud(resourceRequirements);
            spawnCloudAtBottom(cloud);
            randomizeYPosition(cloud);
            clouds.add(cloud);
        }

    }

    public void update(float deltaTime, Jumper jumper) {
        basePosition = jumper.getPosition();
        float currentRoof = basePosition.getY() + HEIGHT_ABOVE;
        for (Cloud cloud : clouds) {
            cloud.update(deltaTime);
            if (cloud.getPosition().getY() > currentRoof) {
                spawnCloudAtBottom(cloud);
            }
        }
    }

    private void spawnCloudAtBottom(Cloud cloud) {
        randomizePosition(cloud);
        randomizeVelocity(cloud);
        randomizeScale(cloud);

    }

    private void randomizeScale(Cloud cloud) {
        cloud.setScale(75* SCALE + random.nextFloat() * 100* SCALE);
    }

    private void randomizeVelocity(Cloud cloud) {
        cloud.setVelocity(new Vector(0, 0, 0));
    }

    private void randomizePosition(Cloud cloud) {
        float x = basePosition.getX() + (random.nextFloat() - 0.5f) * RADIUS;
        float z = basePosition.getZ() + (random.nextFloat() - 0.5f) * RADIUS;
        float y = basePosition.getY() - HEIGHT_BELOW;
        cloud.setPosition(new Vector(x, y, z));
    }

    private void randomizeYPosition(Cloud cloud) {
        float y = basePosition.getY() - HEIGHT_BELOW + random.nextFloat() * (HEIGHT_BELOW + HEIGHT_ABOVE);
        cloud.setPosition(new Vector(cloud.getPosition().getX(), y, cloud.getPosition().getZ()));
    }





}
