package dat367.falling;

public class Player {

    private FallState fallState;

    Vector position, velocity, acceleration;
    Vector lookVector;

    public void update(float deltaTime) {
        FallState newState = fallState.handleFalling(deltaTime, this);
        if (newState != null) {
            this.fallState = newState;
        }
    }

}
