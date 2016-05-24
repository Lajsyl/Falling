package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Ground {

    private Model sea = new Model("sea.g3db");
    private RenderTask seaRenderTask = new ModelRenderTask(sea, new Vector(0, 0, 0), new Rotation(), new Vector(1, 1, 1));

    public Ground(ResourceRequirements resourceRequirements) {
        resourceRequirements.require(sea);
    }

    public void update(float deltaTime) {
        RenderQueue.addTask(seaRenderTask);
    }
}
