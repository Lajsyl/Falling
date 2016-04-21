package dat367.falling.core.world;

import dat367.falling.core.Jumper;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class World {
    private Ground ground;
    private CloudSimulator cloudSimulator;
    private Jumper jumper;

    //defined according to the coordinate system used
    public static final float GRAVITATION = -9.82f;

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        cloudSimulator.update(deltaTime, jumper);
    }

    public World(ResourceRequirements resourceRequirements) {
        // Create jumper using the world start position etc.
        jumper = new Jumper(getStartPosition(), getStartLookDirection());

        cloudSimulator = new CloudSimulator(20, resourceRequirements, jumper);
    }

    public Vector getStartPosition() {
        return new Vector(0f, 0.5f, -1.8f);
    }

    public Vector getStartLookDirection() {
//        return new Vector(0.975f, -0.206f, -0.070f);
        return new Vector(0.975f, 0, -0.070f).normalized();
    }

    public Jumper getJumper() {
        return jumper;
    }
}
