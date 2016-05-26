package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class LevelOne extends BalloonLevel {

    public LevelOne(ResourceRequirements resourceRequirements){
        super(resourceRequirements);
    }

    @Override
    public void create() {
        // Creates a balloons and obstacles from the bottom and up, starting at currentHeight, offsetX and offsetZ
        int currentHeight = 500;
        int offsetX = 100;
        int offsetZ = -100;

        // Create slalom by alternating balloons and obstacles
        for (int i = 0; i < 4; i++) {
            float y = currentHeight;
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, y, offsetZ));
            interactableList.add(balloon);

            y += 22 + 2.5f*i;
            Interactable obstacle = new Mine(resourceRequirements, new Vector(offsetX, y, offsetZ));
            interactableList.add(obstacle);
            currentHeight += (22 + 2.5f*i)*2;
        }
        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight+10, offsetZ)));

        // Height between the slalom and the spiral -10
        currentHeight += 200;
        offsetX += -100;
        offsetZ += 0;

        // Create a spriral pattern of balloons guarded by 2 obstacles each
        for (int i = 0; i < 10; i++) {
            float step = (float) i;
            float x = (float)Math.cos(step) * 60 + offsetX;
            float z = (float)Math.sin(step) * 60 + offsetZ;

            // Every 100 meters from 1000m and up.
            float y = currentHeight;
            currentHeight += 100;

            Interactable balloon = new Balloon(resourceRequirements, new Vector(x, y, z));
            x -= (7+(i/2)-.5f);
            z -= (7+(i/2)-.5f);
            Interactable o1 = new Mine(resourceRequirements, new Vector(x, y, z));
            x += ((7+(i/2))-.5f)*2;
            z += ((7+(i/2))-.5f)*2;
            Interactable o2 = new Mine(resourceRequirements, new Vector(x, y, z));
            interactableList.add(balloon);
            interactableList.add(o1);
            interactableList.add(o2);
        }

        // Space between sprial and next balloon +100
        currentHeight += 50;

        interactableList.add(new Mine(resourceRequirements, new Vector(offsetX-15,currentHeight-23,offsetZ+15)));
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            interactableList.add(balloon);
            currentHeight += 23;
            offsetX += 15;
            offsetZ -= 15;
        }

        // Space between line and next line +23
        currentHeight += 70;

        offsetZ -= 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            interactableList.add(balloon);
            currentHeight += 23;
            offsetX -= 15;
            offsetZ -= 15;
        }

        // Space between line and next line +23
        currentHeight += 95;

        offsetX -= 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            interactableList.add(balloon);
            currentHeight += 23;
            offsetX -= 15;
            offsetZ += 15;
        }

        // Space between line and next line +23
        currentHeight += 120;

        offsetZ += 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            interactableList.add(balloon);
            currentHeight += 23;
            offsetX += 15;
            offsetZ += 15;
        }

        // Space between line and next balloon +23
        currentHeight += 210;
        offsetX += 120;
        offsetZ += 80;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 300;
        offsetX -= 40;
        offsetZ += 200;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 150;
        offsetX -= 60;
        offsetZ -= 70;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 350;
        offsetX -= 40;
        offsetZ -= 200;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 100;
        offsetX += 10;
        offsetZ += 50;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));


        Interactable balloonNearPlane = new Balloon(resourceRequirements, new Vector(-13.3f, 4015, 0));
        interactableList.add(balloonNearPlane);
        Interactable obstacleNearPlane = new Mine(resourceRequirements, new Vector(0, 3930, -50));
        interactableList.add(obstacleNearPlane);
        Interactable obstacleNearPlane2 = new Mine(resourceRequirements, new Vector(-20, 3960, -50));
        interactableList.add(obstacleNearPlane2);
        Interactable obstacleNearPlane3 = new Mine(resourceRequirements, new Vector(-30, 3990, -50));
        interactableList.add(obstacleNearPlane3);
        Interactable obstacleNearPlane4 = new Mine(resourceRequirements, new Vector(-35, 4010, -50));
        interactableList.add(obstacleNearPlane4);
        Interactable obstacleNearPlane5 = new Mine(resourceRequirements, new Vector(-35, 4030, -50));
        interactableList.add(obstacleNearPlane5);
    }

    // TODO: FIX LEVEL DUE TO INTERACTIBLE SIZE DECREASE

    @Override
    public String toString() {
        return "LevelOne";
    }
}
