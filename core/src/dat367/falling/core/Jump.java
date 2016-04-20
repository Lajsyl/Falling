package dat367.falling.core;

import dat367.falling.core.world.World;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Model;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Jump {

    private Jumper jumper;
    private World world;
    private ResourceRequirements resourceRequirements;

    Model airplane = new Model("airplane.g3db");

    public Jump() {
        // Create world
        world = new World();

        // Create jumper using the world start position etc.
        jumper = new Jumper(world.getStartPosition(), world.getStartLookDirection());

        resourceRequirements = new ResourceRequirements();
        resourceRequirements.require(airplane);
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        world.update(deltaTime);

        // In airplane.update()
        RenderQueue.RenderTask task = new RenderQueue.RenderTask(airplane, new Vector(0,0,0),new Vector(0,0,0),new Vector(1,1,1));
        RenderQueue.addTask(task);
    }

    public ResourceRequirements getResourceRequirements() {
        return resourceRequirements;
    }

    public Jumper getJumper() {
        return jumper;
    }
}
