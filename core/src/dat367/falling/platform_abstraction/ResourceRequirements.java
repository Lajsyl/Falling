package dat367.falling.platform_abstraction;

import java.util.ArrayList;
import java.util.List;

public class ResourceRequirements {

    private List<Model> requiredModels = new ArrayList<Model>();
    private List<Quad> requiredQuads = new ArrayList<Quad>();
    private List<Sound> requiredSounds = new ArrayList<Sound>();
    private List<HeightMap> requiredHeightMaps = new ArrayList<HeightMap>();

    public void require(Model model) {
        requiredModels.add(model);
    }

    public void require(Quad quad) {
        requiredQuads.add(quad);
    }
    public void require(Sound sound) {
        requiredSounds.add(sound);
    }
    public void require(HeightMap heightMap) {
        requiredHeightMaps.add(heightMap);
    }
    public List<Model> getModels() {
        return requiredModels;
    }

    public List<Quad> getQuads() {
        return requiredQuads;
    }
    public List<Sound> getSounds() {
        return requiredSounds;
    }
    public List<HeightMap> getHeightMaps() {
        return requiredHeightMaps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceRequirements that = (ResourceRequirements) o;

        if (requiredModels != null ? !requiredModels.equals(that.requiredModels) : that.requiredModels != null)
            return false;
        if (requiredQuads != null ? !requiredQuads.equals(that.requiredQuads) : that.requiredQuads != null)
            return false;
        if (requiredSounds != null ? !requiredSounds.equals(that.requiredSounds) : that.requiredSounds != null)
            return false;
        return requiredHeightMaps != null ? requiredHeightMaps.equals(that.requiredHeightMaps) : that.requiredHeightMaps == null;

    }

    @Override
    public int hashCode() {
        int result = requiredModels != null ? requiredModels.hashCode() : 0;
        result = 31 * result + (requiredQuads != null ? requiredQuads.hashCode() : 0);
        result = 31 * result + (requiredSounds != null ? requiredSounds.hashCode() : 0);
        result = 31 * result + (requiredHeightMaps != null ? requiredHeightMaps.hashCode() : 0);
        return result;
    }
}
