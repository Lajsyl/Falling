package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;
import dat367.falling.platform_abstraction.Sound;

public class World {

    public static final float AIR_DENSITY = 1.2041f; // kg/m3 (at 20°C)

    // Defined according to the coordinate system used
    public static final float GRAVITATION = -9.82f;

    private Ground ground;
    private Island island;
    private CloudSimulator cloudSimulator;
    private Jumper jumper;
    private Airplane airplane;


    public World(ResourceRequirements resourceRequirements) {
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
