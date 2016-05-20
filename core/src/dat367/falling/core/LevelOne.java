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
        for (int i = 0; i < 25; i++) {
            float step = (float) i;
            float x = (float)Math.cos(step) * 60;
            float z = (float)Math.sin(step) * 60;

            // Every 100 meters from 1000m and up.
            float y = step * 100.0f + 1000.0f;

            Collectible c = new Collectible(resourceRequirements, new Vector(x, y, z));
            balloonList.add(c);
        }

        for (int i = 0; i < 25; i++){

            float step = (float) i;

            // Every 100 meters from 1000m and up.
            if(i < 15) {
                float y1 = step * 100.0f + 1000.0f - 25;
                float y2 = step * 100.0f + 1000.0f + 25;
                float x = (float)Math.cos(step) * 60;
                float z = (float)Math.sin(step) * 60;
                Obstacle o1 = new Obstacle(resourceRequirements, new Vector(x, y1, z));
                Obstacle o2 = new Obstacle(resourceRequirements, new Vector(x, y2, z));
                obstacleList.add(o1);
                obstacleList.add(o2);
            }else{
                float y = step * 100.0f + 1000.0f;
                float x1 = (float)Math.cos(step) * 60 + 10;
                float x2 = (float)Math.cos(step) * 60 - 10;
                float z1 = (float)Math.sin(step) * 60 + 10;
                float z2 = (float)Math.sin(step) * 60 - 10;
                Obstacle o1 = new Obstacle(resourceRequirements, new Vector(x1, y, z1));
                Obstacle o2 = new Obstacle(resourceRequirements, new Vector(x2, y, z2));
                obstacleList.add(o1);
                obstacleList.add(o2);
            }
        }
    }

    @Override
    public String toString() {
        return "LevelOne";
    }
}
