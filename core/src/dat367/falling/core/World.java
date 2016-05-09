package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class World {

    private Ground ground;
    private CloudSimulator cloudSimulator;
    private Jumper jumper;
    private Collectible collectible;

    public static final float AIR_DENSITY = 1.2041f; // kg/m3 (at 20°C)

    private Model airplane = new Model("airplane.g3db");

    //defined according to the coordinate system used
    public static final float GRAVITATION = -9.82f;

    public World(ResourceRequirements resourceRequirements) {
        CollisionManager.clear();
        // Create jumper using the world start position etc.
        jumper = new Jumper(getStartPosition(), getStartBodyRotation());

        ground = new Ground(resourceRequirements);
        cloudSimulator = new CloudSimulator(resourceRequirements, jumper);

        collectible = new Collectible(resourceRequirements);

        resourceRequirements.require(airplane);
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        ground.update(deltaTime);
        cloudSimulator.update(deltaTime, jumper);
        collectible.update(deltaTime);

        // In airplane.update()
        RenderTask airplaneTask = new ModelRenderTask(airplane, new Vector(0,4000,0), new Vector(0,0,0), new Vector(1,1,1));
        RenderQueue.addTask(airplaneTask);

        //CollisionManager.update(deltaTime);
    }

    public Vector getStartPosition() {
        return new Vector(0f, 4000.5f, -1.8f);
    }

    public Rotation getStartBodyRotation() {
//        return new Vector(0.975f, -0.206f, -0.070f);
        return new Rotation(new Vector(0.975f, 0, -0.070f).normalized(), new Vector(0, 1, 0));
    }

    public Jumper getJumper() {
        return jumper;
    }
}
