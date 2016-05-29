package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Sound;

public class PinnedPositionedSound extends PositionedSound {

    private final Positioned pinToObject;
    private Vector offsetPosition;

    public PinnedPositionedSound(Sound sound, final Positioned pinToObject, final Vector offsetPosition, float volume) {
        super(sound, pinToObject.getPosition().add(offsetPosition), volume);
        this.pinToObject = pinToObject;
        this.offsetPosition = offsetPosition;
        //Get notification if object this is pinned is moved
        NotificationManager.getDefault().addObserver(pinToObject.getPositionChangedEventID(), new NotificationManager.EventHandler<Positioned>() {
            @Override
            public void handleEvent(NotificationManager.Event<Positioned> event) {
                if (event.data == pinToObject) {
                    updatePosition();
                }
            }
        });
    }

    public PinnedPositionedSound(Sound sound, Positioned pinToObject, Vector offsetPosition) {
        this(sound, pinToObject, offsetPosition, 1.0f);
    }

    public void setOffsetPosition(Vector offsetPosition) {
        this.offsetPosition = offsetPosition;
        updatePosition();
    }

    private void updatePosition() {
        setPosition(pinToObject.getPosition().add(offsetPosition));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PinnedPositionedSound that = (PinnedPositionedSound) o;

        if (pinToObject != null ? !pinToObject.equals(that.pinToObject) : that.pinToObject != null) return false;
        return offsetPosition != null ? offsetPosition.equals(that.offsetPosition) : that.offsetPosition == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (pinToObject != null ? pinToObject.hashCode() : 0);
        result = 31 * result + (offsetPosition != null ? offsetPosition.hashCode() : 0);
        return result;
    }
}
