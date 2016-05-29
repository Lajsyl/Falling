package dat367.falling.core;


import dat367.falling.math.Vector;

public interface Positioned {

    Vector getPosition();
    String getPositionChangedEventID();
    void setEnabled(boolean enabled);
}
