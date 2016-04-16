package dat367.falling;

public class PreJumpState implements FallState {

    final double BACK_ROTATION_MAX = Math.toRadians(45.0);
    final float HIP_TO_HEAD_DISTANCE = 1.3f;

    // How far you have to tilt your head down before the lean-out begins
    final double BEGIN_LEANOUT_AT_PITCH = Math.toRadians(35.0);

    // How far you have to tilt your head down before the lean-out reaches its maximum value
    final double END_LEANOUT_AT_PITCH = Math.toRadians(90.0);

    // How directly your head must be turned towards the door for the lean-out to begin.
    final double BEGIN_LEANOUT_AT_YAW = Math.toRadians(70.0);

    // How far you have to lean out to initiate a jump (given as a fraction of maximum lean-out)
    final double JUMP_THRESHOLD = 0.88;

    Vector hipPosition;

    @Override
    public void setup(Jumper jumper) {
        this.hipPosition = jumper.getPosition().sub(new Vector(0, HIP_TO_HEAD_DISTANCE, 0));
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        final Vector lookDirection = jumper.getLookDirection().normalized();
        final Vector horizontalLookDirection = (new Vector(lookDirection.getX(), 0, lookDirection.getZ())).normalized(); // Look direction projected at the horizontal plane
        final Vector outwards = jumper.getNeutralDirection().normalized();
        final Vector up = new Vector(0, 1, 0);

        float cosPitch = lookDirection.dot(horizontalLookDirection);
        cosPitch = FallingMath.clamp0_1(cosPitch);
        float cosYaw = outwards.dot(horizontalLookDirection);
        cosYaw = FallingMath.clamp0_1(cosYaw);

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
            return new FreeFallingState();
        }

        return null;
    }
}
