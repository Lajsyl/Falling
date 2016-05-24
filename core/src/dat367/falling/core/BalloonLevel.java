package dat367.falling.core;

import dat367.falling.math.Vector;
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

    public void update(float deltaTime, Jumper jumper){
        for (Collectible c : balloonList) {
            if (shouldBeRendered(c, jumper)) {
                c.update(deltaTime);
            }
        }

        for (Obstacle o : obstacleList) {
            if (shouldBeRendered(o, jumper)) {
                o.update(deltaTime);
            }
        }
    }

    private boolean shouldBeRendered(Positioned positioned, Jumper jumper) {
        Vector between = positioned.getPosition().sub(jumper.getPosition());
        return between.length() <= BalloonGameMode.BALLOON_MAX_DRAW_DISTANCE;
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
