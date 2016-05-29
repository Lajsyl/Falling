package dat367.falling.platform;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.UBJsonReader;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;
import dat367.falling.core.NotificationManager;
import dat367.falling.core.PositionedSound;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ResourceHandler {

    private Map<String, ModelInstance> models = new HashMap<String, ModelInstance>();
    private Map<String, TextureAttribute> quadTextureAttributes = new HashMap<String, TextureAttribute>();
    private Map<String, HeightMap.ImageBrightnessData> heightMapImages = new HashMap<String, HeightMap.ImageBrightnessData>();
    private List<AnimationController> animationControllers = new ArrayList<AnimationController>();
    private ModelInstance quadModel;

    private UBJsonReader jsonReader = new UBJsonReader();
    private G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

    public void loadResources(ResourceRequirements resourceRequirements, boolean platformIsAndroid) {
        loadModels(resourceRequirements);
        loadQuads(resourceRequirements);
        loadHeightMaps(resourceRequirements);

    }

    private void loadHeightMaps(ResourceRequirements resourceRequirements) {
        for (HeightMap heightMap : resourceRequirements.getHeightMaps()) {
            if (heightMapImages.containsKey(heightMap.getHeightMapFileName())) {
                heightMap.setImageBrightnessData(heightMapImages.get(heightMap.getHeightMapFileName()));
            } else {
                FileHandle imageFile = Gdx.files.internal(heightMap.getHeightMapFileName());
                InputStream inputStream = imageFile.read();
                try {
                    Gdx2DPixmap gdx2DPixMap = new Gdx2DPixmap(inputStream, Gdx2DPixmap.GDX2D_FORMAT_RGB888);
                    int width = gdx2DPixMap.getWidth();
                    int height = gdx2DPixMap.getHeight();

                    float[][] pixelBrightness = new float[height][width];
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            // Extract the red value from the pixel's RGBA data
                            pixelBrightness[y][x] = (float)(gdx2DPixMap.getPixel(x, y) >>> 24) / 255.0f;
                        }
                    }
                    HeightMap.ImageBrightnessData imageBrightnessData = new HeightMap.ImageBrightnessData(pixelBrightness);
                    heightMapImages.put(heightMap.getHeightMapFileName(), imageBrightnessData);
                    heightMap.setImageBrightnessData(imageBrightnessData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadModels(ResourceRequirements resourceRequirements) {
        for (Model model : resourceRequirements.getModels()) {
                String fileName = model.getModelFileName();
            if (!models.containsKey(fileName)) {

                com.badlogic.gdx.graphics.g3d.Model gdxModel = modelLoader.loadModel(Gdx.files.getFileHandle(fileName, Files.FileType.Internal));
                ModelInstance gdxModelInstance = new ModelInstance(gdxModel);

                int cullFace = model.getShouldCullFaces() ? GL20.GL_BACK : GL20.GL_NONE;
                gdxModelInstance.materials.first().set(IntAttribute.createCullFace(cullFace));

                models.put(fileName, gdxModelInstance);

                // If model has animation, loop it automatically
                if (gdxModelInstance.animations.size > 0) {
                    AnimationController animationController = new AnimationController(gdxModelInstance);
                    animationController.setAnimation(gdxModelInstance.animations.get(gdxModelInstance.animations.size - 1).id, -1, 1.2f, new AnimationController.AnimationListener() {
                        @Override
                        public void onEnd(AnimationController.AnimationDesc animation) {}
                        @Override
                        public void onLoop(AnimationController.AnimationDesc animation) {}
                    });
                    animationControllers.add(animationController);
                }
            }
        }
    }

    private void loadQuads(ResourceRequirements resourceRequirements) {
        // Create the quad model if quads are required
        if (resourceRequirements.getQuads().size() > 0) {

            ModelBuilder modelBuilder = new ModelBuilder();
            final long attributes = VertexAttributes.Usage.Position |
                    VertexAttributes.Usage.Normal |
                    VertexAttributes.Usage.TextureCoordinates;

            com.badlogic.gdx.graphics.g3d.Model quadModelSource = modelBuilder.createRect(
                    // Corners
                    -1, 0, -1,
                    -1, 0, +1,
                    +1, 0, +1,
                    +1, 0, -1,

                    // Normal
                    0, 1, 0,

                    // Material
                    new Material(
                            // (Just add a blend & cull-face attribute, the texture attribute will be set for every render)
                            new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),
                            IntAttribute.createCullFace(GL20.GL_NONE)
                    ),

                    // Attributes
                    attributes
            );
            this.quadModel = new ModelInstance(quadModelSource);
        }

        for (Quad quad : resourceRequirements.getQuads()) {
            String textureFileName = quad.getTextureFileName();
            if (!quadTextureAttributes.containsKey(textureFileName)) {
                FileHandle fileHandle = Gdx.files.internal(textureFileName);
                Texture quadTexture = new Texture(fileHandle, quad.shouldUseMipMaps());

                quadTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

                Texture.TextureFilter minFilter = quad.shouldUseMipMaps()
                        ? Texture.TextureFilter.MipMapLinearLinear
                        : Texture.TextureFilter.Linear;
                quadTexture.setFilter(minFilter, Texture.TextureFilter.Linear);

                TextureAttribute quadTextureAttribute = TextureAttribute.createDiffuse(quadTexture);
                quadTextureAttributes.put(textureFileName, quadTextureAttribute);

                // Report aspect ratio back to Quad
                quad.setAspectRatio((float) quadTexture.getWidth() / (float) quadTexture.getHeight());
            } else {
                Texture quadTexture = quadTextureAttributes.get(textureFileName).textureDescription.texture;
                // Report aspect ratio back to Quad
                quad.setAspectRatio((float) quadTexture.getWidth() / (float) quadTexture.getHeight());
            }
        }
    }

    // It seems that the cardboard coordinate system swaps x and z???
    private Vector convertToCardboardCoordinateSystem(Vector position) {
        return new Vector(position.getZ(), position.getY(), position.getX());
    }

    public Map<String, ModelInstance> getModels(){
        return new HashMap<String, ModelInstance>(models);
    }

    public Map<String, TextureAttribute> getQuadTextureAttributes(){
        return new HashMap<String, TextureAttribute>(quadTextureAttributes);
    }

    public ModelInstance getQuadModel(){
        return new ModelInstance(quadModel);
    }


    public void updateAnimations(float deltaTime) {
        for (AnimationController animationController : animationControllers) {
            animationController.update(Gdx.graphics.getDeltaTime());
        }
    }
}
