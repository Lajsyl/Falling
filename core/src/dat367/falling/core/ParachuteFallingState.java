package dat367.falling.core;


import dat367.falling.core.world.World;
import dat367.falling.math.Vector;


public class ParachuteFallingState implements FallState {

    private Vector parachuteDirection;


    @Override
    public void setup(Jumper jumper) {
        jumper.setNeutralDirection(jumper.getLookDirection().projectedXZ().normalized());
        setParachuteDirection(jumper.getNeutralDirection());
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {

        jumper.setAcceleration(calculateAcceleration(jumper));

        Vector v0 = jumper.getVelocity();

        jumper.setVelocity(calculateVelocity(deltaTime, jumper));
        jumper.setPosition(calculatePosition(deltaTime, jumper, v0));


        if (jumper.getPosition().getY() < 1){
            return new LandedState();
        }

        return null;
    }


    private Vector calculateAcceleration(Jumper jumper){
        return calcAccY(jumper).add(calcAccXZ(jumper));
    }

    //Looks exactly like the one in FreeFalling, except different surface area...
    //TODO adjust for optimal speed
    private Vector calcAccY(Jumper jumper){
        float drag = (float)(0.5*1.0*1.2041*17)*jumper.getVelocity().getY()*jumper.getVelocity().getY();

        float newY = (World.GRAVITATION*90 + drag)/90;

        return new Vector(0,newY,0);
    }


    //TODO steering
    private Vector calcAccXZ(Jumper jumper){

        Vector targetVelocity = parachuteDirection.normalized();
        targetVelocity = targetVelocity.projectedXZ()/*.mirrorY()*/.scale(80);
        float maxSpeed = 35f;
        if (targetVelocity.length() > maxSpeed) {
            targetVelocity = targetVelocity.normalized().scale(maxSpeed);
        }

        Vector currentVelocity = jumper.getVelocity();
        currentVelocity = currentVelocity.projectedXZ();

        Vector newAcc = targetVelocity.sub(currentVelocity);

        return newAcc.scale(10f);

    }

    private Vector calculateVelocity(float deltaTime, Jumper jumper){

        return new Vector(jumper.getVelocity().add(jumper.getAcceleration().scale(deltaTime)));
    }

    private Vector calculatePosition(float deltaTime, Jumper jumper, Vector v0){

        return jumper.getPosition().add(jumper.getVelocity().add(v0).scale(deltaTime/2));
    }

    private void setParachuteDirection(Vector vector){
        parachuteDirection = vector;
    }


}
