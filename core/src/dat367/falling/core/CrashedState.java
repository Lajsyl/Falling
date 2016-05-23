package dat367.falling.core;

import dat367.falling.math.Vector;

public class CrashedState implements FallState {

    public static final String PLAYER_HAS_CRASHED_EVENT_ID = "PLAYER_HAS_CRASHED_EVENT_ID";

    @Override
    public void setup(Jumper jumper) {
        jumper.setAcceleration(new Vector(0, 0, 0));

        Vector position = jumper.getPosition();
        NotificationManager.registerEvent(PLAYER_HAS_CRASHED_EVENT_ID, null);

        // Create shore sound from multiple directions
        for (float angle = 0; angle < 2*Math.PI; angle += 2*Math.PI/3) {
            Vector soundPos = new Vector(position.getX(), 0, position.getZ()).add(new Vector((float)Math.sin(angle)*5, 0, (float)Math.cos(angle)*5));
            PositionedSound shore = new PositionedSound(jumper.shoreSound, soundPos);
            shore.loop();
        }
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
