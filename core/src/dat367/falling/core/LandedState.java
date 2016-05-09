package dat367.falling.core;

import dat367.falling.math.Vector;

public class LandedState implements FallState {
    @Override
    public void setup(Jumper jumper) {

    }

    //Might create a change in position as to mimic the shock when landing
    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        // Save velocity for previous frame for later calculations
        Vector previousFrameVelocity = jumper.getVelocity().projectOntoPlaneXZ();

        jumper.setAcceleration(calculateAcceleration(jumper));
        jumper.setVelocity(calculateVelocity(deltaTime, jumper));
        jumper.setPosition(calculatePosition(deltaTime, jumper, previousFrameVelocity));

        return null;
    }

    public Vector calculateAcceleration(Jumper jumper){
        //Jumper should be stopping, therefore targetVelocity (0,0,0)
        Vector targetVelocity = new Vector(0,0,0);
        Vector currentVelocity = jumper.getVelocity();

        Vector newAcc = targetVelocity.sub(currentVelocity);
        return newAcc.scale(1.1f); //TODO: Check scaling
    }

    public Vector calculateVelocity(float deltaTime, Jumper jumper){

        Vector v = jumper.getVelocity().add(jumper.getAcceleration().scale(deltaTime));
        //Jumper has touched the ground, velocity in Y should always be 0
        v = v.projectOntoPlaneXZ();

        //If the speed is close enough to 0, set vector to (0,0,0)
        if(v.length() < 0.001){
            return new Vector(0,0,0);
        }else
            return v;
    }

    public Vector calculatePosition(float deltaTime, Jumper jumper, Vector v0){
        Vector v = v0.add(jumper.getVelocity().scale(deltaTime/2));
        return v.add(jumper.getPosition());
    }

    @Override
    public String toString() {
        return "Landed";
    }

}
