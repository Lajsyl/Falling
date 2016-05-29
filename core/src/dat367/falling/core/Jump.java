package dat367.falling.core;

import dat367.falling.platform_abstraction.ResourceRequirements;

public class Jump {

    private World world;
    private GameMode gameMode;
    private ResourceRequirements resourceRequirements = new ResourceRequirements();

    public Jump() {
        // Create world
        world = new DefaultWorld(resourceRequirements);
        gameMode = new BalloonGameMode(resourceRequirements, new BalloonTestLevel(resourceRequirements));
    }

    public void update(float deltaTime) {
        world.update(deltaTime);
        gameMode.update(deltaTime, world);
    }

    public ResourceRequirements getResourceRequirements() {
        return resourceRequirements;
    }

    public World getWorld() {
        return world;
    }

    public Jumper getJumper() { return world.getJumper(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jump jump = (Jump) o;

        if (world != null ? !world.equals(jump.world) : jump.world != null) return false;
        if (gameMode != null ? !gameMode.equals(jump.gameMode) : jump.gameMode != null) return false;
        return resourceRequirements != null ? resourceRequirements.equals(jump.resourceRequirements) : jump.resourceRequirements == null;

    }

    @Override
    public int hashCode() {
        int result = world != null ? world.hashCode() : 0;
        result = 31 * result + (gameMode != null ? gameMode.hashCode() : 0);
        result = 31 * result + (resourceRequirements != null ? resourceRequirements.hashCode() : 0);
        return result;
    }
}
