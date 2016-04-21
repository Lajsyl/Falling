package dat367.falling.core;

import dat367.falling.core.world.World;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Jump {

    private Jumper jumper;
    private World world;
    private ResourceRequirements resourceRequirements = new ResourceRequirements();

    public Jump() {

        // Create world
        world = new World(resourceRequirements);

        // Create jumper using the world start position etc.
        jumper = new Jumper(world.getStartPosition(), world.getStartLookDirection());
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        world.update(deltaTime);
    }

    public ResourceRequirements getResourceRequirements() {
        return resourceRequirements;
    }

    public Jumper getJumper() {
        return jumper;
    }
}
