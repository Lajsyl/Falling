package dat367.falling.core;

import dat367.falling.math.FallingMath;
import dat367.falling.math.Vector;

public class PreJumpState implements FallState {

    private final double BACK_ROTATION_MAX = Math.toRadians(45.0);
    private final float HIP_TO_HEAD_DISTANCE = 1.7f; // not very realistic but it looks good.

    // How far you have to tilt your head down before the lean-out begins
    private final double BEGIN_LEANOUT_AT_PITCH = Math.toRadians(35.0);

    // How far you have to tilt your head down before the lean-out reaches its maximum value
    private final double END_LEANOUT_AT_PITCH = Math.toRadians(90.0);

    // How directly your head must be turned towards the door for the lean-out to begin.
    private final double BEGIN_LEANOUT_AT_YAW = Math.toRadians(70.0);

    // How far you have to lean out to initiate a jump (given as a fraction of maximum lean-out)
    private final double JUMP_THRESHOLD = 0.70;

    private final boolean THROW_OUT_JUMPER = false;

    private Vector hipPosition;

    @Override
    public void setup(Jumper jumper) {
        this.hipPosition = jumper.getPosition().sub(new Vector(0, HIP_TO_HEAD_DISTANCE, 0));
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        final Vector lookDirection = jumper.getHeadRotation().getDirection().normalized();
        final Vector horizontalLookDirection = (new Vector(lookDirection.getX(), 0, lookDirection.getZ())).normalized(); // Look direction projected at the horizontal plane
        final Vector outwards = jumper.getBodyRotation().getDirection().normalized();
        final Vector up = new Vector(0, 1, 0);

        float cosPitch = lookDirection.dot(horizontalLookDirection);
        cosPitch = FallingMath.clamp01(cosPitch);
        float cosYaw = outwards.dot(horizontalLookDirection);
        cosYaw = FallingMath.clamp01(cosYaw);

        double interpolation;
        if (lookDirection.getY() > 0) {
            interpolation = 0;
        } else {
            double pitch = Math.acos(cosPitch);
            pitch = FallingMath.clamp(pitch, BEGIN_LEANOUT_AT_PITCH, END_LEANOUT_AT_PITCH) - BEGIN_LEANOUT_AT_PITCH;
            double leanoutPitchRange = END_LEANOUT_AT_PITCH - BEGIN_LEANOUT_AT_PITCH;
            double pitchInterpolation = FallingMath.interpolateSmooth(pitch / leanoutPitchRange);

            double yaw = Math.acos(cosYaw);
            yaw = Math.min(yaw, BEGIN_LEANOUT_AT_YAW);
            double yawInterpolation = (yaw / BEGIN_LEANOUT_AT_YAW);
            yawInterpolation = Math.cos(yawInterpolation * Math.PI / 2);

            interpolation = pitchInterpolation * yawInterpolation;
        }

        // Lean out of the plane by tilting the player's back
        double backRotation = interpolation * BACK_ROTATION_MAX;
        Vector outwardsComponent = outwards.scale(HIP_TO_HEAD_DISTANCE * (float) Math.sin(backRotation));
        Vector upComponent       =       up.scale(HIP_TO_HEAD_DISTANCE * (float) Math.cos(backRotation));
        Vector finalHeadPosition = hipPosition.add(outwardsComponent).add(upComponent);
        jumper.setPosition(finalHeadPosition);

        // When the player leans out far enough, jump!
        if (interpolation > JUMP_THRESHOLD) {
            if (THROW_OUT_JUMPER) {
                jumper.setVelocity(20, 0, 0);
            }
            return new FreeFallingState();
        }

        // OR if the player makes a fast enough motion outwards, throw them out of the plane!
//        Vector headwards = jumper.getHeadRotation().getUp();
//        headwards.getX() =
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
