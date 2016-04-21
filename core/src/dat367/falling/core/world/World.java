package dat367.falling.core.world;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class World {
    private Ground ground;

    Model airplane = new Model("airplane.g3db");
    Quad cloud = new Quad("cloud_01.png", true, true);

    //defined according to the coordinate system used
    public static final float GRAVITATION = -9.82f;

    public World(ResourceRequirements resourceRequirements) {
        ground = new Ground(resourceRequirements);

        resourceRequirements.require(airplane);
        resourceRequirements.require(cloud);
    }

    public void update(float deltaTime) {
        ground.update(deltaTime);

        // In airplane.update()
        RenderTask airplaneTask = new ModelRenderTask(airplane, new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1));
        RenderQueue.addTask(airplaneTask);

        // In cloud.update() or similar
        RenderTask cloudTask = new QuadRenderTask(cloud, new Vector(0,-30,0), new Vector(0,0,0), new Vector(10,10,10));
        RenderQueue.addTask(cloudTask);
    }

    public Vector getStartPosition() {
        return new Vector(0f, 0.5f, -1.8f);
    }

    public Vector getStartLookDirection() {
//        return new Vector(0.975f, -0.206f, -0.070f);
        return new Vector(0.975f, 0, -0.070f).normalized();
    }
}
