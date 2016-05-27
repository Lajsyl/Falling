package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class DefaultWorld implements World {

    private Ground ground;
    private Island island;
    private CloudSimulator cloudSimulator;
    private Jumper jumper;
    private Airplane airplane;


    public DefaultWorld(ResourceRequirements resourceRequirements) {
        CollisionManager.clear();

        airplane = new Airplane(resourceRequirements, new Vector(0, 4000, 0));
        ground = new Ground(resourceRequirements);
        island = new Island(resourceRequirements, new Vector(0,0,0));

        // Create jumper using the airplane metrics
        jumper = new Jumper(resourceRequirements, airplane.getHeadStartPosition(), airplane.getLookOutDirection());

        cloudSimulator = new CloudSimulator(resourceRequirements, jumper);

    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        ground.update(deltaTime);
        island.update(deltaTime);
        cloudSimulator.update(deltaTime, jumper, airplane);
        airplane.update(deltaTime);

        CollisionManager.update(deltaTime);
    }

    public Jumper getJumper() {
        return jumper;
    }
}
