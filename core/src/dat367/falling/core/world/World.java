package dat367.falling.core.world;

import dat367.falling.core.Jumper;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class World {

    private Ground ground;
    private CloudSimulator cloudSimulator;
    private Jumper jumper;

    public static final float AIR_DENSITY = 1.2041f; // kg/m3 (at 20°C)

    private Model airplane = new Model("airplane.g3db");

    //defined according to the coordinate system used
    public static final float GRAVITATION = -9.82f;

    public World(ResourceRequirements resourceRequirements) {
        // Create jumper using the world start position etc.
        jumper = new Jumper(getStartPosition(), getStartLookDirection());

        ground = new Ground(resourceRequirements);
        cloudSimulator = new CloudSimulator(resourceRequirements, jumper);

        resourceRequirements.require(airplane);
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        ground.update(deltaTime);
        cloudSimulator.update(deltaTime, jumper);

        // In airplane.update()
        RenderTask airplaneTask = new ModelRenderTask(airplane, new Vector(0,4000,0), new Vector(0,0,0), new Vector(1,1,1));
        RenderQueue.addTask(airplaneTask);
    }

    public Vector getStartPosition() {
        return new Vector(0f, 4000.5f, -1.8f);
    }

    public Vector getStartLookDirection() {
//        return new Vector(0.975f, -0.206f, -0.070f);
        return new Vector(0.975f, 0, -0.070f).normalized();
    }

    public Jumper getJumper() {
        return jumper;
    }
}
