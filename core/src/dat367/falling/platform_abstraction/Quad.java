package dat367.falling.platform_abstraction;

public class Quad {

    private final String textureFileName;
    private final boolean aspectRatioAdjust;
    private final boolean useMipMaps;

    // Will be set by the platform layer loader
    private Float aspectRatio = null;

    public Quad(String textureFileName) {
        this(textureFileName, true, true);
    }

    public Quad(String textureFileName, boolean aspectRatioAdjust, boolean useMipMaps) {
        this.textureFileName = textureFileName;
        this.aspectRatioAdjust = aspectRatioAdjust;
        this.useMipMaps = useMipMaps;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public boolean shouldAspectRatioAdjust() {
        return aspectRatioAdjust;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Float getAspectRatio() {
        return aspectRatio;
    }

    public boolean shouldUseMipMaps() {
        return useMipMaps;
    }
}
