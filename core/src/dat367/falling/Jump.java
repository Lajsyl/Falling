package dat367.falling;

import java.util.List;

public class Jump {

    private Player player;
    private World world;
    private ResourceRequirements resourceRequirements;

    Model zombie = new Model("airplane.g3db");

    public Jump() {
        player = new Player();
        world = new World();
        resourceRequirements = new ResourceRequirements();
        resourceRequirements.require(zombie);
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        world.update(deltaTime);

        // In zombie.update()
        RenderQueue.RenderTask task = new RenderQueue.RenderTask(zombie, new Vector(0,0,0),new Vector(0,0,0),new Vector(1,1,1));
        RenderQueue.addTask(task);
    }

    public ResourceRequirements getResourceRequirements() {
        return resourceRequirements;
    }
}
