package dat367.falling.platform;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Quaternion;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;
import dat367.falling.core.NotificationManager;
import dat367.falling.core.PositionedSound;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;
import dat367.falling.platform_abstraction.Sound;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AudioHandler {

    Camera mainCamera;
    private CardboardAudioEngine cardboardAudioEngine;

    private Set<String> preloadedSounds = new HashSet<String>();
    private List<Integer> loopingSounds = new ArrayList<Integer>();

    private ResourceRequirements resourceRequirements;

    public AudioHandler(Camera mainCamera, ResourceRequirements resourceRequirements){
        this.mainCamera = mainCamera;
        this.resourceRequirements = resourceRequirements;
    }

    public void updateHeadPlacementForPositionalSound(Rotation head) {
        Vector position = convertToCardboardCoordinateSystem(new Vector(mainCamera.position.x, mainCamera.position.y, mainCamera.position.z));
        cardboardAudioEngine.setHeadPosition(position.getX(), position.getY(), position.getZ());
        Vector xAxis = head.getDirection();
        Vector yAxis = head.getUp();
        Vector zAxis = head.getRight();
        Quaternion headQuaternion = new Quaternion().setFromAxes(xAxis.getX(), xAxis.getY(), xAxis.getZ(),
                yAxis.getX(), yAxis.getY(), yAxis.getZ(),
                zAxis.getX(), zAxis.getY(), zAxis.getZ());
        cardboardAudioEngine.setHeadRotation(headQuaternion.x, headQuaternion.y, headQuaternion.z, headQuaternion.w);
    }

    public void loadAudio(boolean platformIsAndroid){
        if (platformIsAndroid) {
            loadSounds(resourceRequirements);
        }
    }


    public void setCardboardAudioEngine(CardboardAudioEngine cardboardAudioEngine) {
        this.cardboardAudioEngine = cardboardAudioEngine;
    }


    private void loadSounds(ResourceRequirements resourceRequirements) {
        for (Sound sound : resourceRequirements.getSounds()) {
            String fileName = sound.getSoundFileName();
            if (!preloadedSounds.contains(fileName)) {
                cardboardAudioEngine.preloadSoundFile(fileName);
                preloadedSounds.add(fileName);
            }
        }
    }


    public void setupSoundEventHandling() {
        NotificationManager.getDefault().addObserver(PositionedSound.PLAY_SOUND_EVENT, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                makeSoundAvailable(event.data);
                cardboardAudioEngine.playSound(event.data.getSoundObjectID(), false);
            }
        });
        NotificationManager.getDefault().addObserver(PositionedSound.LOOP_SOUND_EVENT, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                makeSoundAvailable(event.data);
                cardboardAudioEngine.playSound(event.data.getSoundObjectID(), true);
                loopingSounds.add(event.data.getSoundObjectID());
            }
        });
        NotificationManager.getDefault().addObserver(PositionedSound.STOP_SOUND_EVENT, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                if (event.data.getSoundObjectID() != -1) {
                    cardboardAudioEngine.stopSound(event.data.getSoundObjectID());
                }
            }
        });
        NotificationManager.getDefault().addObserver(PositionedSound.CHANGE_POSITION_SOUND_EVENT, new NotificationManager.EventHandler<PositionedSound>() {
            @Override
            public void handleEvent(NotificationManager.Event<PositionedSound> event) {
                if (event.data.getSoundObjectID() != -1) {
                    Vector pos = convertToCardboardCoordinateSystem(event.data.getPosition());
                    cardboardAudioEngine.setSoundObjectPosition(event.data.getSoundObjectID(), pos.getX(), pos.getY(), pos.getZ());
                }
            }
        });
        NotificationManager.getDefault().addObserver(PositionedSound.CHANGE_VOLUME_SOUND_EVENT, new NotificationManager.EventHandler<PositionedSound>() {
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
                System.out.println(sound);
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

    public void stopAllLoopingSounds() {
        while (!loopingSounds.isEmpty()) {
            cardboardAudioEngine.stopSound(loopingSounds.remove(0));
        }
    }


    public  void pause(){
        cardboardAudioEngine.pause();
    }

    public void resume(){
        cardboardAudioEngine.resume();
    }


    // It seems that the cardboard coordinate system swaps x and z???
    private Vector convertToCardboardCoordinateSystem(Vector position) {
        return new Vector(position.getZ(), position.getY(), position.getX());
    }


}
