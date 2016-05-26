package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Sound;

// TODO Make cardboardaudio position update according to jumper pos

public class PositionedSound {
    private Sound sound;
    private Vector position;
    private float volume;
    private int soundObjectID = -1;

    public static final String PLAY_SOUND_EVENT = "PlaySoundEvent";
    public static final String LOOP_SOUND_EVENT = "LoopSoundEvent";
    public static final String STOP_SOUND_EVENT = "StopSoundEvent";
    public static final String CHANGE_POSITION_SOUND_EVENT = "ChangePositionSoundEvent";
    public static final String CHANGE_VOLUME_SOUND_EVENT = "ChangeVolumeSoundEvent";

    public PositionedSound(Sound sound, Vector position, float volume) {
        this.sound = sound;
        this.position = position;
        this.volume = volume;
    }

    public PositionedSound(Sound sound, Vector position) {
        this(sound, position, 1.0f);
    }

    public void play() {
        NotificationManager.getDefault().registerEvent(PLAY_SOUND_EVENT, this);
    }

    public void loop() {
        NotificationManager.getDefault().registerEvent(LOOP_SOUND_EVENT, this);
    }

    public void stop() {
        NotificationManager.getDefault().registerEvent(STOP_SOUND_EVENT, this);
    }

//    new PositionedSound(new Sound("wind01.wav"), new Vector(0, 4000, 0), 1.0).play()


    public int getSoundObjectID() {
        return soundObjectID;
    }

    public void setSoundObjectID(int soundObjectID) {
        this.soundObjectID = soundObjectID;
    }

    public String getSoundFileName() {
        return sound.getSoundFileName();
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
        NotificationManager.getDefault().registerEvent(CHANGE_POSITION_SOUND_EVENT, this);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        NotificationManager.getDefault().registerEvent(CHANGE_VOLUME_SOUND_EVENT, this);
    }
}
