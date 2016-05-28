package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class BalloonTestLevel extends BalloonLevel {

    public BalloonTestLevel(ResourceRequirements resourceRequirements){
        super(resourceRequirements);
    }

    @Override
    public void create() {
        // Creates a balloons and obstacles from the bottom and up, starting at currentHeight, offsetX and offsetZ
        int currentHeight = 500;
        int offsetX = 150;
        int offsetZ = -50;

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
            x -= (2.75f+.25f*i);
            z -= (2.75f+.25f*i);
            Interactable o1 = new Mine(resourceRequirements, new Vector(x, y, z));
            x += (2.75f+.25f*i)*2;
            z += (2.75f+.25f*i)*2;
            Interactable o2 = new Mine(resourceRequirements, new Vector(x, y, z));
            interactableList.add(balloon);
            interactableList.add(o1);
            interactableList.add(o2);
        }

        // Space between sprial and next balloon +100
        currentHeight += 30;

        interactableList.add(new Mine(resourceRequirements, new Vector(offsetX-11,currentHeight-21,offsetZ+11)));
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            interactableList.add(balloon);
            currentHeight += 25;
            offsetX += 15;
            offsetZ -= 15;
        }

        // Space between line and next line +25
        currentHeight += 60;

        offsetZ -= 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            interactableList.add(balloon);
            currentHeight += 25;
            offsetX -= 15;
            offsetZ -= 15;
        }

        // Space between line and next line +25
        currentHeight += 65;

        offsetX -= 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            interactableList.add(balloon);
            currentHeight += 25;
            offsetX -= 15;
            offsetZ += 15;
        }

        // Space between line and next line +25
        currentHeight += 70;

        offsetZ += 30;
        // Create steep line of balloons
        for (int i = 0; i < 3; i++){
            Interactable balloon = new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ));
            interactableList.add(balloon);
            currentHeight += 25;
            offsetX += 15;
            offsetZ += 15;
        }

        // Space between line and next balloon +23
        currentHeight += 190;
        offsetX += 120;
        offsetZ += 80;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 120;
        offsetX += 10;
        offsetZ += 90;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 200;
        offsetX -= 50;
        offsetZ += 120;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 150;
        offsetX -= 60;
        offsetZ -= 70;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 160;
        offsetX -= 15;
        offsetZ -= 90;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 190;
        offsetX -= 25;
        offsetZ -= 110;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        currentHeight += 100;
        offsetX += 10;
        offsetZ += 50;

        interactableList.add(new Balloon(resourceRequirements, new Vector(offsetX, currentHeight, offsetZ)));

        // Make interactibles (except the the two closest below) initially disabled to improve frame rate in airplane
        for (Interactable interactable : interactableList) {
            interactable.setEnabled(false);
        }

        Interactable balloonNearPlane = new Balloon(resourceRequirements, new Vector(-2.8f, 4005-300, 0));
        interactableList.add(balloonNearPlane);
        Interactable obstacleNearPlane = new Mine(resourceRequirements, new Vector(0, 3950-300, -28.5f), 1.2f);
        interactableList.add(obstacleNearPlane);
    }

    @Override
    public String toString() {
        return "BalloonTestLevel";
    }
}
