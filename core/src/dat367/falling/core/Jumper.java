package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;

import java.util.Observable;

public class Jumper extends Observable {

    private FallState fallState = new PreJumpState();

    private Vector position;
    private Vector velocity = new Vector(0, 0, 0);
    private Vector acceleration = new Vector(0, 0, 0);
    private Rotation bodyRotation;
    private Rotation headRotation;
//    private Vector lookDirection;
//    private Vector upVector = new Vector(0, 1, 0);
    private boolean screenClicked = false;

    public Jumper(Vector position, Rotation bodyRotation) {
        this.position = position;

        this.bodyRotation = bodyRotation;

//        this.rotation = rotation;

//        this.neutralLookDirection = neutralLookDirection;
//        this.lookDirection = neutralLookDirection;

        fallState.setup(this);
    }

    public void update(float deltaTime) {
        FallState newState = fallState.handleFalling(deltaTime, this);
        if (newState != null) {
            this.fallState = newState;
            fallState.setup(this);
        }
    }

    public Vector getLookDirection() {
        return headRotation.getDirection();
    }

    public void setAcceleration(float x, float y, float z){
        acceleration = new Vector(x, y, z);
    }

    public void setVelocity(float x, float y, float z){
        velocity = new Vector(x, y, z);
    }

    public void setPosition(float x, float y, float z){ position = new Vector(x,y,z);}

    public void setLookDirection(Vector lookDirection) {
        this.lookDirection = lookDirection;
    }

    public void setUpVector(Vector upVector) {
        this.upVector = upVector;
    }

    public Vector getUpVector(){
        return upVector;
    }

    public Vector getNeutralLookDirection() {
        return neutralLookDirection;
    }

    public void setNeutralLookDirection(Vector neutralLookDirection) {
        this.neutralLookDirection = neutralLookDirection;
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

    public FallState getFallState(){ return fallState; }

    public void setFallState(FallState fallState){ this.fallState = fallState; }

    public void setScreenClicked(boolean screenClicked){
        this.screenClicked = screenClicked;
        setChanged();
        notifyObservers();
    }

    public boolean getScreenClicked(){
        return screenClicked;
    }

    public String getFallStateDebugString() {
        return fallState.toString();
    }

    public void setHeadRotation(Rotation headRotation) {
        this.headRotation = headRotation;
    }

    public Rotation getBodyRotation() {
        return bodyRotation;
    }
}
