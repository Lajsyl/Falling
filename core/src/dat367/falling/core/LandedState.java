package dat367.falling.core;

import dat367.falling.math.Vector;

/**
 * Created by Lisa on 2016-05-01.
 */
public class LandedState implements FallState {
    @Override
    public void setup(Jumper jumper) {

    }

    //Might create a change in postion as to mimic the shock when landing
    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        Vector v0 = jumper.getVelocity();

        jumper.setAcceleration(calculateAcceleration(jumper));
        jumper.setVelocity(calculateVelocity(deltaTime, jumper));
        jumper.setPosition(calculatePosition(deltaTime, jumper, v0));

        return null;
    }

    public Vector calculateAcceleration(Jumper jumper){
        Vector targetVelocity = new Vector(0,0,0);
        Vector currentVelocity = jumper.getVelocity();
        currentVelocity = currentVelocity.projectedXZ();
        Vector newAcc = targetVelocity.sub(currentVelocity);

        return newAcc.scale(10f);
    }

    public Vector calculateVelocity(float deltaTime, Jumper jumper){
        return jumper.getVelocity().add(jumper.getAcceleration().scale(deltaTime));
    }

    public Vector calculatePosition(float deltaTime, Jumper jumper, Vector v0){
        Vector v = v0.add(jumper.getVelocity().scale(deltaTime/2));
        return v.add(jumper.getPosition());
    }

}
