package dat367.falling.platform_abstraction;

public class Quad {

    private final String textureFileName;

    private final float uvXScale;
    private final float uvYScale;

    private final boolean aspectRatioAdjust;
    private final boolean useMipMaps;
    private final boolean isOpaque;

    private final float maxDrawDistance;
    private final float fadeOutDistance;

    // NOTE: Will be set by the platform layer after image is loaded!
    private Float aspectRatio = null;

    public Quad(String textureFileName) {
        this(textureFileName, true, true, 400, 50, 1f, 1f, false);
    }

    public Quad(String textureFileName, boolean aspectRatioAdjust, boolean useMipMaps,
                float maxDrawDistance, float fadeOutDistance, float uvXScale, float uvYScale,
                boolean isOpaque) {
        this.textureFileName = textureFileName;

        this.uvXScale = uvXScale;
        this.uvYScale = uvYScale;

        this.aspectRatioAdjust = aspectRatioAdjust;
        this.useMipMaps = useMipMaps;
        this.isOpaque = isOpaque;

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

    public boolean isOpaque() {
        return isOpaque;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quad quad = (Quad) o;

        // Don't include aspectRatio: it's derived data from the file name
        if (Float.compare(quad.uvXScale, uvXScale) != 0) return false;
        if (Float.compare(quad.uvYScale, uvYScale) != 0) return false;
        if (aspectRatioAdjust != quad.aspectRatioAdjust) return false;
        if (useMipMaps != quad.useMipMaps) return false;
        if (isOpaque != quad.isOpaque) return false;
        if (Float.compare(quad.maxDrawDistance, maxDrawDistance) != 0) return false;
        if (Float.compare(quad.fadeOutDistance, fadeOutDistance) != 0) return false;
        return textureFileName != null ? textureFileName.equals(quad.textureFileName) : quad.textureFileName == null;

    }

    @Override
    public int hashCode() {
        int result = textureFileName != null ? textureFileName.hashCode() : 0;
        result = 31 * result + (uvXScale != +0.0f ? Float.floatToIntBits(uvXScale) : 0);
        result = 31 * result + (uvYScale != +0.0f ? Float.floatToIntBits(uvYScale) : 0);
        result = 31 * result + (aspectRatioAdjust ? 1 : 0);
        result = 31 * result + (useMipMaps ? 1 : 0);
        result = 31 * result + (isOpaque ? 1 : 0);
        result = 31 * result + (maxDrawDistance != +0.0f ? Float.floatToIntBits(maxDrawDistance) : 0);
        result = 31 * result + (fadeOutDistance != +0.0f ? Float.floatToIntBits(fadeOutDistance) : 0);
        return result;
    }
}
