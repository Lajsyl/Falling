package dat367.falling.platform_abstraction;

public class Model {

    private String modelFileName;
    private boolean cullFaces = true;

    public Model(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public Model(String modelFileName, boolean cullFaces) {
        this.modelFileName = modelFileName;
        this.cullFaces = cullFaces;
    }

    public String getModelFileName() {
        return modelFileName;
    }

    public boolean getShouldCullFaces() {
        return cullFaces;
    }
}
