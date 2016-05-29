package dat367.falling.platform_abstraction;

public class HeightMap {
    private String heightMapFileName;
    private ImageBrightnessData imageBrightnessData;

    public HeightMap(String heightMapFileName) {
        this.heightMapFileName = heightMapFileName;
    }

    public void setImageBrightnessData(ImageBrightnessData imageBrightnessData) {
        this.imageBrightnessData = imageBrightnessData;
    }

    public String getHeightMapFileName() {
        return heightMapFileName;
    }

    public float getBrightnessAtPixel(int x, int y) {
        return imageBrightnessData.getBrightness(x, y);
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
        return imageBrightnessData.getImageWidth();
    }

    public int getImageHeight() {
        return imageBrightnessData.getImageHeight();
    }

    public static class ImageBrightnessData {
        private float[][] pixelBrightness;

        public ImageBrightnessData(float[][] pixelBrightness) {
            this.pixelBrightness = new float[pixelBrightness.length][pixelBrightness[0].length];
            for (int y = 0; y < pixelBrightness.length; y++) {
                for (int x = 0; x < pixelBrightness[0].length; x++) {
                    this.pixelBrightness[y][x] = pixelBrightness[y][x];
                }
            }
        }

        public float getBrightness(int x, int y) {
            return pixelBrightness[y][x];
        }

        public int getImageWidth() {
            return pixelBrightness[0].length;
        }

        public int getImageHeight() {
            return pixelBrightness.length;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeightMap heightMap = (HeightMap) o;

        // Don't count brightness data, the data is derived from file name
        return heightMapFileName != null ? heightMapFileName.equals(heightMap.heightMapFileName) : heightMap.heightMapFileName == null;

    }

    @Override
    public int hashCode() {
        return heightMapFileName != null ? heightMapFileName.hashCode() : 0;
    }
}
