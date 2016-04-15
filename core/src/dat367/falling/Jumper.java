package dat367.falling;

public class Jumper {

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
}
