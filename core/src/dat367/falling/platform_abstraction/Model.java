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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        if (cullFaces != model.cullFaces) return false;
        if (shouldFadeOut != model.shouldFadeOut) return false;
        if (Float.compare(model.maxDrawDistance, maxDrawDistance) != 0) return false;
        if (Float.compare(model.fadeOutDistance, fadeOutDistance) != 0) return false;
        return modelFileName != null ? modelFileName.equals(model.modelFileName) : model.modelFileName == null;

    }

    @Override
    public int hashCode() {
        int result = modelFileName != null ? modelFileName.hashCode() : 0;
        result = 31 * result + (cullFaces ? 1 : 0);
        result = 31 * result + (shouldFadeOut ? 1 : 0);
        result = 31 * result + (maxDrawDistance != +0.0f ? Float.floatToIntBits(maxDrawDistance) : 0);
        result = 31 * result + (fadeOutDistance != +0.0f ? Float.floatToIntBits(fadeOutDistance) : 0);
        return result;
    }
}
