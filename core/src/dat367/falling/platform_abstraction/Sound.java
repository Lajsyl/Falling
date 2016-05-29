package dat367.falling.platform_abstraction;

public class Sound {

    private String soundFileName;

    public Sound(String soundFileName) {
        this.soundFileName = soundFileName;
    }

    public String getSoundFileName() {
        return soundFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sound sound = (Sound) o;

        return soundFileName != null ? soundFileName.equals(sound.soundFileName) : sound.soundFileName == null;

    }

    @Override
    public int hashCode() {
        return soundFileName != null ? soundFileName.hashCode() : 0;
    }
}
