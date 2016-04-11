package dat367.falling;

public class Player {

    private FallState fallState = new PreJumpState();

    Vector position, velocity, acceleration;
    Vector neutralDirection;
    Vector lookDirection;

    public void update(float deltaTime) {
        FallState newState = fallState.handleFalling(deltaTime, this);
        if (newState != null) {
            this.fallState = newState;
        }
        System.out.println(lookDirection);
    }

    public void setLookDirection(Vector lookDirection) {
        this.lookDirection = lookDirection;
    }
}
