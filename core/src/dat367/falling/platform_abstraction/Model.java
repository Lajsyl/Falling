package dat367.falling.platform_abstraction;

public class Model {

    private String modelFileName;

    private boolean visible;

    public Model(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public String getModelFileName() {
        return modelFileName;
    }
}
