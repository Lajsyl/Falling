package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Sound;

// TODO Make cardboardaudio position update according to jumper pos

public class PositionedSound {
    private Sound sound;
    private Vector position;
    private float volume;
    private int soundObjectID = -1;

    public static final String playSoundEvent = "PlaySoundEvent";
    public static final String loopSoundEvent = "LoopSoundEvent";
    public static final String stopSoundEvent = "StopSoundEvent";
    public static final String changePositionSoundEvent = "ChangePositionSoundEvent";
    public static final String changeVolumeSoundEvent = "ChangeVolumeSoundEvent";

    public PositionedSound(Sound sound, Vector position, float volume) {
        this.sound = sound;
        this.position = position;
        this.volume = volume;
    }

    public PositionedSound(Sound sound, Vector position) {
        this(sound, position, 1.0f);
    }

    public void play() {
        NotificationManager.registerEvent(playSoundEvent, this);
    }

    public void loop() {
        NotificationManager.registerEvent(loopSoundEvent, this);
    }

    public void stop() {
        NotificationManager.registerEvent(stopSoundEvent, this);
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
        NotificationManager.registerEvent(changePositionSoundEvent, this);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        NotificationManager.registerEvent(changeVolumeSoundEvent, this);
    }
}
