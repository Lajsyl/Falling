package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.List;

public class LevelOne extends BalloonLevel {

    public LevelOne(ResourceRequirements resourceRequirements){
        super(resourceRequirements);
    }

    @Override
    public void create() {
        // Creates a balloons and obstacles from the bottom and up, starting at currentHeight, offsetX and offsetZ
        int currentHeight = 500;
        // TODO: Set offset so that they end at a good point
        int offsetX = 100;
        int offsetZ = -100;

        // Create slalom by alternating balloons and obstacles
        for (int i = 0; i < 4; i++) {
            float y = currentHeight;
            Collectible balloon = new Collectible(resourceRequirements, new Vector(offsetX, y, offsetZ));
            balloonList.add(balloon);

            y += 40;
            Obstacle obstacle = new Obstacle(resourceRequirements, new Vector(offsetX, y, offsetZ));
            obstacleList.add(obstacle);
            currentHeight += 80;
        }
        balloonList.add(new Collectible(resourceRequirements, new Vector(offsetX, currentHeight+10, offsetZ)));

        // Height between the slalom and the spiral -10
        currentHeight += 200;

        // Create a spriral pattern of balloons guarded by 2 obstacles each
        for (int i = 0; i < 10; i++) {
            float step = (float) i;
            float x = (float)Math.cos(step) * 60 + offsetX;
            float z = (float)Math.sin(step) * 60 + offsetZ;

            // Every 100 meters from 1000m and up.
            float y = currentHeight;
            currentHeight += 100;

            Collectible balloon = new Collectible(resourceRequirements, new Vector(x, y, z));
            x -= (7+(i/2));
            z -= (7+(i/2));
            Obstacle o1 = new Obstacle(resourceRequirements, new Vector(x, y, z));
            x += ((7+(i/2))*2);
            z += ((7+(i/2))*2);
            Obstacle o2 = new Obstacle(resourceRequirements, new Vector(x, y, z));
            balloonList.add(balloon);
            obstacleList.add(o1);
            obstacleList.add(o2);
            // TODO: Maybe add spin to the way the obstacles are placed
        }

        // Space between sprial and next balloon +100
        currentHeight += 50;

        //TODO: Lägg till en rad med (inte jättemånga) ballonger där hinder är på ena sidan, och på den sista är det på andra sidan

        //TODO: Fix so offset is okay

        obstacleList.add(new Obstacle(resourceRequirements, new Vector(offsetX-15,currentHeight-23,offsetZ+15)));
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Collectible balloon = new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            balloonList.add(balloon);
            currentHeight += 23;
            offsetX += 15;
            offsetZ -= 15;
        }

        // Space between line and next line +23
        currentHeight += 70;

        offsetZ -= 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Collectible balloon = new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            balloonList.add(balloon);
            currentHeight += 23;
            offsetX -= 15;
            offsetZ -= 15;
        }

        // Space between line and next line +23
        currentHeight += 95;

        offsetX -= 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Collectible balloon = new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            balloonList.add(balloon);
            currentHeight += 23;
            offsetX -= 15;
            offsetZ += 15;
        }

        // Space between line and next line +23
        currentHeight += 120;

        offsetZ += 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Collectible balloon = new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            balloonList.add(balloon);
            currentHeight += 23;
            offsetX += 15;
            offsetZ += 15;
        }

        /*
        // Space between line and next balloon +23
        currentHeight += 210;
        offsetX += 120;
        offsetZ += 80;

        balloonList.add(new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 300;
        offsetX -= 40;
        offsetZ += 200;

        balloonList.add(new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 150;
        offsetX -= 60;
        offsetZ -= 70;

        balloonList.add(new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 350;
        offsetX -= 40;
        offsetZ -= 200;

        balloonList.add(new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 100;
        offsetX += 10;
        offsetZ += 50;

        balloonList.add(new Collectible(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));*/

        Collectible balloonNearPlane = new Collectible(resourceRequirements, new Vector(-13.3f, 4015, 0));
        balloonList.add(balloonNearPlane);
        Obstacle obstacleNearPlane = new Obstacle(resourceRequirements, new Vector(0, 3930, -50));
        obstacleList.add(obstacleNearPlane);
        Obstacle obstacleNearPlane2 = new Obstacle(resourceRequirements, new Vector(-20, 3960, -50));
        obstacleList.add(obstacleNearPlane2);
        Obstacle obstacleNearPlane3 = new Obstacle(resourceRequirements, new Vector(-30, 3990, -50));
        obstacleList.add(obstacleNearPlane3);
        Obstacle obstacleNearPlane4 = new Obstacle(resourceRequirements, new Vector(-35, 4010, -50));
        obstacleList.add(obstacleNearPlane4);
        Obstacle obstacleNearPlane5 = new Obstacle(resourceRequirements, new Vector(-35, 4030, -50));
        obstacleList.add(obstacleNearPlane5);

    }

    @Override
    public String toString() {
        return "LevelOne";
    }
}
