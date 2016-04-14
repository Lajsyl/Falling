package dat367.falling;

public class PreJumpState implements FallState {

    //(final double REQUIRED_DEGREES = 90.0f;
    //final double COS_REQUIRED = Math.cos(Math.toRadians(REQUIRED_DEGREES));

    final double BACK_ROTATION_MAX = Math.toRadians(35.0);
    final float HIP_TO_HEAD_DISTANCE = 0.75f;

    Vector hipPosition;
    Vector neutralDirection;

    @Override
    public void setup(Jumper jumper) {
        this.hipPosition = jumper.getPosition().sub(new Vector(0, HIP_TO_HEAD_DISTANCE, 0));
        this.neutralDirection = jumper.getLookDirection().normalized();
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {

        // Get look direction
        float cosDeviation = jumper.getLookDirection().normalized().dot(neutralDirection);
        double angleDeviation = Math.acos(cosDeviation);
        double interpolation = angleDeviation / (Math.PI / 2.0);

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
                .add(neutralDirection.scale(rotatedHeadPosition.getZ()))
                .add(hipToHead.scale(rotatedHeadPosition.getY()));

        // TODO: Uncomment!
        //jumper.setPosition(finalHeadPosition);

        // If look direction is "big" enough
        if (interpolation >= 1.0f) {
            // switch to FreeFallingState
            return new FreeFallingState();
        }

        return null;
    }
}
