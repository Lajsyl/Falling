package dat367.falling.core;

import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.ArrayList;
import java.util.List;

public abstract class BalloonLevel {

    List<Collectible> balloonList = new ArrayList<Collectible>();
    List<Obstacle> obstacleList = new ArrayList<Obstacle>();
    ResourceRequirements resourceRequirements;

    public BalloonLevel(ResourceRequirements resourceRequirements){
        this.resourceRequirements = resourceRequirements;
    }

    public abstract void create();

    public void update(float deltaTime){
        for (Collectible c : balloonList) {
            c.update(deltaTime);
        }

        for (Obstacle o : obstacleList) {
            o.update(deltaTime);
        }
    }

    public List<Collectible> getBalloonList(){
        return balloonList;
    }
    public List<Obstacle> getObstacleList(){
        return obstacleList;
    }

    public ResourceRequirements getResourceRequirements(){
        return resourceRequirements;
    }
}
