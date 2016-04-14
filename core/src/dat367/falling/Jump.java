package dat367.falling;

public class Jump {

    private Jumper jumper;
    private World world;
    private ResourceRequirements resourceRequirements;

    Model zombie = new Model("airplane.g3db");

    public Jump() {
        // Create world
        world = new World();

        // Create jumper using the world start position etc.
        jumper = new Jumper(world.getStartPosition(), world.getStartLookDirection());

        resourceRequirements = new ResourceRequirements();
        resourceRequirements.require(zombie);
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        world.update(deltaTime);

        // In zombie.update()
        RenderQueue.RenderTask task = new RenderQueue.RenderTask(zombie, new Vector(0,0,0),new Vector(0,0,0),new Vector(1,1,1));
        RenderQueue.addTask(task);
    }

    public ResourceRequirements getResourceRequirements() {
        return resourceRequirements;
    }

    public Jumper getJumper() {
        return jumper;
    }
}
