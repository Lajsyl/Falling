package dat367.falling;

import java.util.ArrayList;
import java.util.List;

public class ResourceRequirements {
    private List<Model> requiredModels = new ArrayList<Model>();

    public void require(Model model) {
        requiredModels.add(model);
    }

    public List<Model> getModels() {
        return requiredModels;
    }
}
