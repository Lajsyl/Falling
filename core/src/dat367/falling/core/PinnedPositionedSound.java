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

}
