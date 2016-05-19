package dat367.falling.platform;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.UBJsonReader;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;
import dat367.falling.core.NotificationManager;
import dat367.falling.core.PositionedSound;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Model;
import dat367.falling.platform_abstraction.Quad;
import dat367.falling.platform_abstraction.ResourceRequirements;
import dat367.falling.platform_abstraction.Sound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResourceHandler {

    private Map<String, ModelInstance> models = new HashMap<String, ModelInstance>();
    private Map<String, TextureAttribute> quadTextureAttributes = new HashMap<String, TextureAttribute>();
    private Set<String> preloadedSounds = new HashSet<String>();
    private ModelInstance quadModel;

    private UBJsonReader jsonReader = new UBJsonReader();
    private G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

    private CardboardAudioEngine cardboardAudioEngine;





    public void loadResources(ResourceRequirements resourceRequirements, boolean platformIsAndroid) {
        loadModels(resourceRequirements);
        loadQuads(resourceRequirements);
        if (platformIsAndroid) {
            loadSounds(resourceRequirements);
        }
    }

    private void loadModels(ResourceRequirements resourceRequirements) {
        for (Model model : resourceRequirements.getModels()) {
            String fileName = model.getModelFileName();
            if (!models.containsKey(fileName)) {
                com.badlogic.gdx.graphics.g3d.Model gdxModel = modelLoader.loadModel(Gdx.files.getFileHandle(fileName, Files.FileType.Internal));
                ModelInstance gdxModelInstance = new ModelInstance(gdxModel);
                models.put(fileName, gdxModelInstance);
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
            }
        }
    }

    private void loadSounds(ResourceRequirements resourceRequirements) {
        for (Sound sound : resourceRequirements.getSounds()) {
            String fileName = sound.getSoundFileName();
            if (!preloadedSounds.contains(sound.getSoundFileName())) {
                cardboardAudioEngine.preloadSoundFile(sound.getSoundFileName());
                preloadedSounds.add(sound.getSoundFileName());
            }
        }
    }


    public void setupSoundEventHandling() {
        NotificationManager.addObserver(PositionedSound.playSoundEvent, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                makeSoundAvailable(event.data);
                cardboardAudioEngine.playSound(event.data.getSoundObjectID(), false);
            }
        });
        NotificationManager.addObserver(PositionedSound.loopSoundEvent, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                makeSoundAvailable(event.data);
                cardboardAudioEngine.playSound(event.data.getSoundObjectID(), true);
            }
        });
        NotificationManager.addObserver(PositionedSound.stopSoundEvent, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                if (event.data.getSoundObjectID() != -1) {
                    cardboardAudioEngine.stopSound(event.data.getSoundObjectID());
                }
            }
        });
        NotificationManager.addObserver(PositionedSound.changePositionSoundEvent, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                if (event.data.getSoundObjectID() != -1) {
                    Vector pos = convertToCardboardCoordinateSystem(event.data.getPosition());
                    cardboardAudioEngine.setSoundObjectPosition(event.data.getSoundObjectID(), pos.getX(), pos.getY(), pos.getZ());
                }
            }
        });
        NotificationManager.addObserver(PositionedSound.changeVolumeSoundEvent, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                if (event.data.getSoundObjectID() != -1) {
                    cardboardAudioEngine.setSoundVolume(event.data.getSoundObjectID(), event.data.getVolume());
                }
            }
        });
    }

    private void makeSoundAvailable(PositionedSound sound) {
        if (sound.getSoundObjectID() == -1) { // If cardboard sound object does not exist, create it
            if (!preloadedSounds.contains(sound.getSoundFileName())) { // If sound is not preloaded, load it
                cardboardAudioEngine.preloadSoundFile(sound.getSoundFileName());
                preloadedSounds.add(sound.getSoundFileName());
            }
            createCardboardSoundObject(sound);
        }
    }

    private void createCardboardSoundObject(PositionedSound positionedSound) {
        positionedSound.setSoundObjectID(cardboardAudioEngine.createSoundObject(positionedSound.getSoundFileName()));
        Vector pos = convertToCardboardCoordinateSystem(positionedSound.getPosition());
        cardboardAudioEngine.setSoundObjectPosition(positionedSound.getSoundObjectID(), pos.getX(), pos.getY(), pos.getZ());
        cardboardAudioEngine.setSoundVolume(positionedSound.getSoundObjectID(), positionedSound.getVolume());
    }



    // It seems that the cardboard coordinate system swaps x and z???
    private Vector convertToCardboardCoordinateSystem(Vector position) {
        return new Vector(position.getZ(), position.getY(), position.getX());
    }


    public Map<String, ModelInstance> getModels(){
        return new HashMap(models);
    }

    public Map<String, TextureAttribute> getQuadTextureAttributes(){
        return new HashMap(quadTextureAttributes);
    }

    public ModelInstance getQuadModel(){
        return new ModelInstance(quadModel);
    }

}
