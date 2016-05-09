package dat367.falling.core;

import dat367.falling.platform_abstraction.ResourceRequirements;

public class Jump {

    private World world;
    private ResourceRequirements resourceRequirements = new ResourceRequirements();

    public Jump() {

        // Create world
        world = new World(resourceRequirements);
    }

    public void update(float deltaTime) {
        world.update(deltaTime);
    }

    public ResourceRequirements getResourceRequirements() {
        return resourceRequirements;
    }

    public World getWorld() {
        return world;
    }

    public Jumper getJumper() {
        return world.getJumper();
    }
}
