package dat367.falling.platform_abstraction;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;

public class ModelRenderTask extends RenderTask {

    private final Model model;

    public ModelRenderTask(Model model, Vector position, Rotation rotation, Vector scale) {
        super(position, rotation, scale);
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelRenderTask that = (ModelRenderTask) o;

        return model != null ? model.equals(that.model) : that.model == null;

    }

    @Override
    public int hashCode() {
        return model != null ? model.hashCode() : 0;
    }
}
