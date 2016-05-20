package dat367.falling.core;

import dat367.falling.math.Vector;

public class CrashedState implements FallState {

    public static final String PLAYER_HAS_CRASHED_EVENT_ID = "PLAYER_HAS_CRASHED_EVENT_ID";

    @Override
    public void setup(Jumper jumper) {
        jumper.setAcceleration(new Vector(0, 0, 0));

        Vector position = jumper.getPosition();
        NotificationManager.registerEvent(PLAYER_HAS_CRASHED_EVENT_ID, null);
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        return null;
    }

    @Override
    public String toString() {
        return "Crashed...";
    }
}
