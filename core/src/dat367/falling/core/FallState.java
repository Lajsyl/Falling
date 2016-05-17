package dat367.falling.core;

public interface FallState {

    String STATE_CHANGED_EVENT_ID = "FallStateChangedEventID";

    void setup(Jumper jumper);
    FallState handleFalling(float deltaTime, Jumper jumper);
}
