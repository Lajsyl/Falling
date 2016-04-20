package dat367.falling.core;

import dat367.falling.core.world.World;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Jump {

    private Jumper jumper;
    private World world;
    private ResourceRequirements resourceRequirements;

    Model airplane = new Model("airplane.g3db");
    Quad cloud = new Quad("cloud_01.png", true);

    public Jump() {
        // Create world
        world = new World();

        // Create jumper using the world start position etc.
        jumper = new Jumper(world.getStartPosition(), world.getStartLookDirection());

        resourceRequirements = new ResourceRequirements();
        resourceRequirements.require(airplane);
        resourceRequirements.require(cloud);
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        world.update(deltaTime);

        // In airplane.update()
        RenderTask airplaneTask = new ModelRenderTask(airplane, new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1));
        RenderQueue.addTask(airplaneTask);

        // In cloud.update() or similar
        RenderTask cloudTask = new QuadRenderTask(cloud, new Vector(0,-30,0), new Vector(0,0,0), new Vector(10,10,10));
        RenderQueue.addTask(cloudTask);
    }

    public ResourceRequirements getResourceRequirements() {
        return resourceRequirements;
    }

    public Jumper getJumper() {
        return jumper;
    }
}
