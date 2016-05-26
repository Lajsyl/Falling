package dat367.falling.core;

import dat367.falling.math.FallingMath;
import dat367.falling.math.Vector;

public class PreJumpState implements FallState {

    public static final String PLAYER_HAS_JUMPED_EVENT_ID = "PLAYER_HAS_JUMPED_EVENT_ID";

    private final double BACK_ROTATION_MAX = Math.toRadians(75.0);
    private final float HIP_TO_HEAD_DISTANCE = 0.8f; // should be as big as leanout arc radius
    private final float LEANOUT_ARC_HEIGHT = 0.5f; // the shape of the arc when leaning out
    private final float LEANOUT_ARC_DEPTH = 0.8f; // the shape of the arc when leaning out

    private final boolean THROW_OUT_JUMPER = false;

    private final float JUMP_THRESHOLD = 0.93f;

    public static final boolean JUMP_IN_REAL_LIFE_TO_JUMP_FROM_PLANE = false;
    public static final boolean LOOK_DOWN_TO_JUMP_FROM_PLANE = true;

    private PositionedSound airplaneWind;
    private PositionedSound airplaneLeanoutWind;

    private Vector hipPosition;

    @Override
    public void setup(Jumper jumper) {
        this.hipPosition = jumper.getPosition().sub(new Vector(0, LEANOUT_ARC_HEIGHT, 0));

        airplaneWind = new PositionedSound(jumper.airplaneWindSound, jumper.getPosition().add(new Vector(2, 0, 0)));
        airplaneWind.loop();
        airplaneLeanoutWind = new PositionedSound(jumper.airplaneLeanoutWindSound, jumper.getPosition().add(new Vector(2, 0, 0.4f)), 0.0f);
        airplaneLeanoutWind.loop();
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        final Vector lookDirection = jumper.getHeadRotation().getDirection().normalized();
        final Vector headUpDirection = jumper.getHeadRotation().getUp();
        final Vector outwards = jumper.getBodyRotation().getDirection().normalized();
        final Vector up = new Vector(0, 1, 0);
        final Vector down = new Vector(0, -1, 0);

        double interpolation = FallingMath.clamp01(outwards.dot(headUpDirection));

        // The more you lean out of the plane, the louder the outside wind noise will be
        airplaneLeanoutWind.setVolume((0.1f + (float)interpolation * 0.9f) * 1.1f);
        airplaneWind.setVolume((1.0f - (float)interpolation) * 0.4f);

        // Lean out of the plane by tilting the player's back
        double backRotation = interpolation * BACK_ROTATION_MAX;
        Vector outwardsComponent = outwards.scale(LEANOUT_ARC_DEPTH * (float) Math.sin(backRotation));
        Vector upComponent       =       up.scale(LEANOUT_ARC_HEIGHT * (float) Math.cos(backRotation));
        Vector finalHeadPosition = hipPosition.add(outwardsComponent).add(upComponent);
        jumper.setPosition(finalHeadPosition);

        if (JUMP_IN_REAL_LIFE_TO_JUMP_FROM_PLANE) {
            // If the player makes a jump in reality, jump them out of the plane in the game
            /*if (jumper.getScreenClicked()) {
                if (THROW_OUT_JUMPER) {
                    jumper.setVelocity(20, 0, 0);
                }
                NotificationManager.getDefault().registerEvent(PLAYER_HAS_JUMPED_EVENT_ID, null);
                return new FreeFallingState();
            }*/
        }
        if (LOOK_DOWN_TO_JUMP_FROM_PLANE) {
            // If the player leans out far enough, jump!
            double lookDownAmount = FallingMath.clamp01(down.dot(lookDirection));
            if (headUpDirection.getY() > 0 && lookDownAmount > JUMP_THRESHOLD) {
                if (THROW_OUT_JUMPER) {
                    jumper.setVelocity(20, 0, 0);
                }
                NotificationManager.getDefault().registerEvent(PLAYER_HAS_JUMPED_EVENT_ID, null);
                return new FreeFallingState();
            }
        }


        return null;
    }

    @Override
    public String toString() {
        return "About to jump";
    }
}
