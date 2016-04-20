package dat367.falling.platform_abstraction;

import dat367.falling.math.Vector;

public class ModelRenderTask extends RenderTask {

    private final Model model;

    public ModelRenderTask(Model model, Vector position, Vector orientation, Vector scale) {
        super(position, orientation, scale);
        this.model = model;
    }

    public Model getModel() {
        return model;
    }
}
