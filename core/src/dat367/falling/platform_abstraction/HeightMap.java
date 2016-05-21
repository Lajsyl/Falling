package dat367.falling.platform_abstraction;

public class HeightMap {
    private String heightMapFileName;
    private float[][] pixelBrightness;

    public HeightMap(String heightMapFileName) {
        this.heightMapFileName = heightMapFileName;
    }

    public void setHeightMapData(float[][] pixelBrightness) {
        this.pixelBrightness = pixelBrightness;
    }

    public String getHeightMapFileName() {
        return heightMapFileName;
    }

    public float getBrightnessAtPixel(int x, int y) {
        return pixelBrightness[y][x];
    }

    // Get brightness data for non-integer image coordinates by interpolating the closest pixels
    public float getInterpolatedBrightnessAt(float x, float y) {
        int intX = (int)x;
        int intY = (int)y;
        float fracX = x - intX;
        float fracY = y - intY;
        float pixelTL = getBrightnessAtPixel(intX, intY);
        float pixelTR = getBrightnessAtPixel(intX + 1, intY);
        float pixelBL = getBrightnessAtPixel(intX, intY + 1);
        float pixelBR = getBrightnessAtPixel(intX + 1, intY + 1);
        float interpolationTop = pixelTL * (1 - fracX) + pixelTR * fracX;
        float interpolationBottom = pixelBL * (1 - fracX) + pixelBR * fracX;
        float interpolation = interpolationTop * (1 - fracY) + interpolationBottom * fracY;
        return interpolation;
    }

    public int getImageWidth() {
        return pixelBrightness[0].length;
    }

    public int getImageHeight() {
        return pixelBrightness.length;
    }
}
