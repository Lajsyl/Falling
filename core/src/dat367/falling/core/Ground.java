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
        RenderQueue.getDefault().addTask(seaRenderTask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ground ground = (Ground) o;

        if (sea != null ? !sea.equals(ground.sea) : ground.sea != null) return false;
        return seaRenderTask != null ? seaRenderTask.equals(ground.seaRenderTask) : ground.seaRenderTask == null;

    }

    @Override
    public int hashCode() {
        int result = sea != null ? sea.hashCode() : 0;
        result = 31 * result + (seaRenderTask != null ? seaRenderTask.hashCode() : 0);
        return result;
    }
}
