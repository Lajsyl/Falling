package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class World {

    public static final float AIR_DENSITY = 1.2041f; // kg/m3 (at 20°C)

    // Defined according to the coordinate system used
    public static final float GRAVITATION = -9.82f;

    private Ground ground;
    private CloudSimulator cloudSimulator;
    private Jumper jumper;
    private Airplane airplane;

    public World(ResourceRequirements resourceRequirements) {
        CollisionManager.clear();

        airplane = new Airplane(resourceRequirements, new Vector(0, 4000, 0));
        ground = new Ground(resourceRequirements);

        // Create jumper using the airplane metrics
        jumper = new Jumper(airplane.getHeadStartPosition(), airplane.getLookOutDirection());

        cloudSimulator = new CloudSimulator(resourceRequirements, jumper);

    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        ground.update(deltaTime);
        cloudSimulator.update(deltaTime, jumper, airplane);
        airplane.update(deltaTime);

        CollisionManager.update(deltaTime);
    }

    public Jumper getJumper() {
        return jumper;
    }
}
