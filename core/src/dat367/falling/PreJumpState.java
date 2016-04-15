package dat367.falling;

public class PreJumpState implements FallState {

    final double BACK_ROTATION_MAX = Math.toRadians(35.0);
    final float HIP_TO_HEAD_DISTANCE = 0.75f;

    Vector hipPosition;

    @Override
    public void setup(Jumper jumper) {
        this.hipPosition = jumper.getPosition().sub(new Vector(0, HIP_TO_HEAD_DISTANCE, 0));
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {

        float cosDeviation = jumper.getLookDirection().normalized().dot(jumper.getNeutralDirection().normalized());
        cosDeviation = FallingMath.clamp0_1(cosDeviation);

        double angleDeviation = Math.acos(cosDeviation);
        float interpolation = (float) (angleDeviation / (Math.PI / 2.0));
        interpolation = FallingMath.clamp0_1(interpolation);

        // Move camera position depending on the look direction (in local base)
        double actualBackRotation = interpolation * BACK_ROTATION_MAX;
        Vector rotatedHeadPosition = new Vector(
                0.0f,
                HIP_TO_HEAD_DISTANCE * (float)Math.cos(actualBackRotation),
                HIP_TO_HEAD_DISTANCE * (float)Math.sin(actualBackRotation)
        );

        // Convert to default base
        final Vector hipToHead = new Vector(0, HIP_TO_HEAD_DISTANCE, 0);
        Vector finalHeadPosition = hipPosition
                .add(jumper.getNeutralDirection().scale(rotatedHeadPosition.getZ()))
                .add(hipToHead.scale(rotatedHeadPosition.getY()));

        jumper.setPosition(finalHeadPosition);

        // If look direction is "big" enough
        if (interpolation >= 1.0f) {
            // switch to FreeFallingState
            return null;//new FreeFallingState();
        }

        return null;
    }
}
