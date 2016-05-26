package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.GUITextTask;
import dat367.falling.platform_abstraction.RenderQueue;

public class CrashedState implements FallState {

    public static final String PLAYER_HAS_CRASHED_EVENT_ID = "PLAYER_HAS_CRASHED_EVENT_ID";

    private float delay = 1.0f;
    private boolean parachutePulled;

    public CrashedState(boolean parachutePulled){
        this.parachutePulled = parachutePulled;
    }

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

        if(delay>0){
            delay -= deltaTime;
        }else {
            String message;
            if (parachutePulled){
                message = "        Beware of\n    where you land...";
            }else{
                message = "      Do not forget\nto pull the parachute...";
            }

            GUITextTask crashMessage =
                    new GUITextTask("  You have crashed!\n" + message + "\n\n    Tap the screen\n        to restart", new Vector(200,200,200), new Vector(0.505f, 0, .608f), true, false);
            RenderQueue.addGUITask(crashMessage);
        }


        return null;
    }

    @Override
    public String toString() {
        return "Crashed...";
    }
}
