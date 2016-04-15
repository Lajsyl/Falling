package dat367.falling;

public class Jumper {

    private FallState fallState = new PreJumpState();

    private Vector position, velocity, acceleration;
    private Vector neutralDirection;
    private Vector lookDirection;

    public Jumper(Vector position, Vector neutralDirection) {
        this.position = position;

        this.neutralDirection = neutralDirection;
        this.lookDirection = neutralDirection;

        fallState.setup(this);
    }

    public void update(float deltaTime) {
        FallState newState = fallState.handleFalling(deltaTime, this);
        if (newState != null) {
            this.fallState = newState;
        }
    }

    public Vector getLookDirection() {
        return lookDirection;
    }


    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public Vector getNeutralDirection(){
        return neutralDirection;
    }

    public Vector getLookDirection() { return lookDirection; }

    public void setAcceleration(float x, float y, float z){
        acceleration = new Vector(x, y, z);
    }

    public void setAcceleration(Vector v){
        setAcceleration(v.getX(), v.getY(), v.getZ());
    }

    public void setVelocity(float x, float y, float z){
        velocity = new Vector(x, y, z);
    }

    public void setVelocity(Vector v){
        setVelocity(v.getX(), v.getY(), v.getZ());
    }

    public void setPosition(float x, float y, float z){ position = new Vector(x,y,z);}

    public void setPosition(Vector v){
        setVelocity(v.getX(), v.getY(), v.getZ());
    }

    public void setLookDirection(Vector lookDirection) {
        this.lookDirection = lookDirection;
    }

    public Vector getNeutralDirection() {
        return neutralDirection;
    }

    public void setNeutralDirection(Vector neutralDirection) {
        this.neutralDirection = neutralDirection;
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
