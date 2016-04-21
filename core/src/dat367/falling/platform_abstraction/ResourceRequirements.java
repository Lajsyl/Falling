package dat367.falling.platform_abstraction;

import java.util.ArrayList;
import java.util.List;

public class ResourceRequirements {

    private List<Model> requiredModels = new ArrayList<Model>();
    private List<Quad> requiredQuads = new ArrayList<Quad>();

    public void require(Model model) {
        requiredModels.add(model);
    }
    public void require(Quad quad) {
        requiredQuads.add(quad);
    }

    public List<Model> getModels() {
        return requiredModels;
    }
    public List<Quad> getQuads() {
        return requiredQuads;
    }

}
