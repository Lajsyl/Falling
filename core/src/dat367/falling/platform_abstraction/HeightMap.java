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
}
