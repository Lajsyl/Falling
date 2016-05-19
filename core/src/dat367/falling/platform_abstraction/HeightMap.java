package dat367.falling.platform_abstraction;

public class Heightmap {
    private String heightMapFileName;

    public Heightmap(String heightMapFileName) {
        this.heightMapFileName = heightMapFileName;
    }

    public String getHeightMapFileName() {
        return heightMapFileName;
    }
}
