package dat367.falling.platform_abstraction;

public class Model {

    private final String modelFileName;
    private final boolean cullFaces;

    private final boolean shouldFadeOut;
    private final float maxDrawDistance;
    private final float fadeOutDistance;

    public Model(String modelFileName) {
        this(modelFileName, true);
    }

    public Model(String modelFileName, boolean cullFaces) {
        this(modelFileName, cullFaces, false, 0, 0);
    }

    public Model(String modelFileName, boolean cullFaces, boolean shouldFadeOut, float maxDrawDistance, float fadeOutDistance) {
        this.modelFileName = modelFileName;
        this.cullFaces = cullFaces;

        this.shouldFadeOut = shouldFadeOut;
        this.maxDrawDistance = maxDrawDistance;
        this.fadeOutDistance = fadeOutDistance;
    }

    public String getModelFileName() {
        return modelFileName;
    }

    public boolean getShouldCullFaces() {
        return cullFaces;
    }

    public boolean shouldFadeOut() {
        return shouldFadeOut;
    }

    public float getMaxDrawDistance() {
        return maxDrawDistance;
    }

    public float getFadeOutDistance() {
        return fadeOutDistance;
    }
}
