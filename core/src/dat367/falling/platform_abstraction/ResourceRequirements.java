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

}
