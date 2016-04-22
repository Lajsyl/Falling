package dat367.falling.core.world;

import dat367.falling.core.Jumper;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudSimulator {
    List<Cloud> clouds = new ArrayList<Cloud>();
    int numberOfClouds;
    private Vector basePosition;
    private float heightAbove = 300;
    private float heightBelow = 500;
    private float radius = 600;
    private Random random = new Random();

    public CloudSimulator(int numberOfClouds, ResourceRequirements resourceRequirements, Jumper jumper) {
        this.numberOfClouds = numberOfClouds;
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
        float currentRoof = basePosition.getY() + heightAbove;
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
        cloud.setScale(75 + random.nextFloat() * 100);
    }

    private void randomizeVelocity(Cloud cloud) {
        cloud.setVelocity(new Vector(0, 0, 0));
    }

    private void randomizePosition(Cloud cloud) {
        float x = basePosition.getX() + (random.nextFloat() - 0.5f) * radius;
        float z = basePosition.getZ() + (random.nextFloat() - 0.5f) * radius;
        float y = basePosition.getY() - heightBelow;
        cloud.setPosition(new Vector(x, y, z));
    }

    private void randomizeYPosition(Cloud cloud) {
        float y = basePosition.getY() - heightBelow + random.nextFloat() * (heightBelow + heightAbove);
        cloud.setPosition(new Vector(cloud.getPosition().getX(), y, cloud.getPosition().getZ()));
    }





}
