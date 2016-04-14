package dat367.falling;

public class Jumper {

    private FallState fallState = new PreJumpState();

    private Vector position, velocity, acceleration;
    private Vector neutralDirection;
    private Vector lookDirection;

    public Jumper(Vector position, Vector neutralDirection) {
        this.position = position;
        this.neutralDirection = neutralDirection;
        fallState.setup(this);
    }

    public void update(float deltaTime) {
        FallState newState = fallState.handleFalling(deltaTime, this);
        if (newState != null) {
            this.fallState = newState;
        }
        System.out.println(lookDirection);
    }

    public Vector getLookDirection() {
        return lookDirection;
    }

    public void setLookDirection(Vector lookDirection) {
        this.lookDirection = lookDirection;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }
}
