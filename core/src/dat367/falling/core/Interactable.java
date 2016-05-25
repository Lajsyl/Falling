package dat367.falling.core;

import dat367.falling.math.Vector;

public abstract class Interactable implements Positioned{

    protected Collider collider;
    protected Vector position;

    protected boolean enabled = true;

    @Override
    public Vector getPosition() {
        return position;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public abstract String getPositionChangedEventID();

    public abstract String getInteractableID();

    public void setPosition(Vector position) {
        this.position = position;
        NotificationManager.registerEvent(getPositionChangedEventID(), this);
    }

    public abstract void update(float deltaTime);
}
