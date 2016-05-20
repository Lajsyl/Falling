package dat367.falling.platform_abstraction;

public class HeightMap {
    private String heightMapFileName;
    private float[][] pixelBrightness;
//    private float pixelBrightnessMinValue;
//    private float pixelBrightnessMaxValue;

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

    public int getImageWidth() {
        return pixelBrightness[0].length;
    }

    public int getImageHeight() {
        return pixelBrightness.length;
    }

//    public float getHighestPixelBrightnessValue() {
//        return pixelBrightnessMaxValue;
//    }
//
//    public float getLowestPixelBrightnessValue() {
//        return pixelBrightnessMinValue;
//    }
//
//    public void setHighestPixelBrightnessValue(float pixelBrightnessMaxValue) {
//        this.pixelBrightnessMaxValue = pixelBrightnessMaxValue;
//    }
//
//    public void setLowestPixelBrightnessValue(float pixelBrightnessMinValue) {
//        this.pixelBrightnessMinValue = pixelBrightnessMinValue;
//    }
}
