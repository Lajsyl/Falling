package dat367.falling.core;


import dat367.falling.math.Vector;


public class ParachuteFallingState implements FallState {

    @Override
    public void setup(Jumper jumper) {

    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {

        jumper.setAcceleration(calculateAcceleration(jumper));

        Vector v0 = jumper.getVelocity(); //maybe??

        jumper.setVelocity(calculateVelocity(jumper));
        jumper.setPosition(calculatePosition(jumper));


        //TODO: if landed, do something

        return null;
    }


    private Vector calculateAcceleration(Jumper jumper){

        return null;
    }

    private Vector calculateVelocity(Jumper jumper){

        return null;
    }

    private Vector calculatePosition(Jumper jumper){

        return null;
    }


}
