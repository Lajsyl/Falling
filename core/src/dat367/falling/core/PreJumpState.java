package dat367.falling.core;

import dat367.falling.math.FallingMath;
import dat367.falling.math.Vector;

public class PreJumpState implements FallState {

    private final double BACK_ROTATION_MAX = Math.toRadians(75.0);
    private final float HIP_TO_HEAD_DISTANCE = 0.8f; // should be as big as leanout arc radius
    private final float LEANOUT_ARC_HEIGHT = 0.5f; // the shape of the arc when leaning out
    private final float LEANOUT_ARC_DEPTH = 0.8f; // the shape of the arc when leaning out

    private final boolean THROW_OUT_JUMPER = false;

    private Vector hipPosition;

    @Override
    public void setup(Jumper jumper) {
        this.hipPosition = jumper.getPosition().sub(new Vector(0, LEANOUT_ARC_HEIGHT, 0));
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        final Vector lookDirection = jumper.getHeadRotation().getDirection().normalized();
        final Vector outwards = jumper.getBodyRotation().getDirection().normalized();
        final Vector up = new Vector(0, 1, 0);

        double interpolation = FallingMath.clamp01(outwards.dot(jumper.getHeadRotation().getUp()));

        // Lean out of the plane by tilting the player's back
        double backRotation = interpolation * BACK_ROTATION_MAX;
        Vector outwardsComponent = outwards.scale(LEANOUT_ARC_DEPTH * (float) Math.sin(backRotation));
        Vector upComponent       =       up.scale(LEANOUT_ARC_HEIGHT * (float) Math.cos(backRotation));
        Vector finalHeadPosition = hipPosition.add(outwardsComponent).add(upComponent);
        jumper.setPosition(finalHeadPosition);


        // If the player makes a jump in reality, jump them out of the plane in the game
        if (jumper.getScreenClicked()) {
            if (THROW_OUT_JUMPER) {
                jumper.setVelocity(20, 0, 0);
            }
            return new FreeFallingState();
        }

        return null;
    }

    @Override
    public String toString() {
        return "About to jump";
    }
}
