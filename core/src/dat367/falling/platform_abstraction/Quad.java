package dat367.falling.platform_abstraction;

public class Quad {

    private final String textureFileName;

    private final float uvXScale;
    private final float uvYScale;

    private final boolean aspectRatioAdjust;
    private final boolean useMipMaps;

    private final float maxDrawDistance;
    private final float fadeOutDistance;

    // NOTE: Will be set by the platform layer after image is loaded!
    private Float aspectRatio = null;

    public Quad(String textureFileName) {
        this(textureFileName, true, true, 400, 50, 1f, 1f);
    }

    public Quad(String textureFileName, boolean aspectRatioAdjust, boolean useMipMaps,
                float maxDrawDistance, float fadeOutDistance, float uvXScale, float uvYScale) {
        this.textureFileName = textureFileName;

        this.uvXScale = uvXScale;
        this.uvYScale = uvYScale;

        this.aspectRatioAdjust = aspectRatioAdjust;
        this.useMipMaps = useMipMaps;

        this.maxDrawDistance = maxDrawDistance;
        this.fadeOutDistance = fadeOutDistance;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public boolean shouldAspectRatioAdjust() {
        return aspectRatioAdjust;
    }
    public Float getAspectRatio() {
        return aspectRatio;
    }
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public boolean shouldUseMipMaps() {
        return useMipMaps;
    }

    public float getMaxDrawDistance() {
        return maxDrawDistance;
    }
    public float getFadeOutDistance() {
        return fadeOutDistance;
    }

    public float getUvXScale() {
        return uvXScale;
    }
    public float getUvYScale() {
        return uvYScale;
    }
}
