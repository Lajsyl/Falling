package dat367.falling.core;


import dat367.falling.core.world.World;
import dat367.falling.math.Vector;


public class ParachuteFallingState implements FallState {

    private Vector parachuteDirection;


    @Override
    public void setup(Jumper jumper) {
        jumper.setNeutralDirection(jumper.getLookDirection().projectedXZ().normalized());
        setParachuteDirection(jumper.getNeutralDirection());
        jumper.setVelocity(jumper.getNeutralDirection().scale(1000));

    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        System.out.println("handle Falling");
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
        System.out.println("calculateAcceleration");
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

        Vector rightDirection = parachuteDirection.cross(jumper.getUpVector());

        Vector projected = jumper.getUpVector().lineProjection(rightDirection);

        Float rollAmount = rightDirection.sub(projected).length()-1;
        System.out.println("rollAmount = " + rollAmount);

        Vector targetVelocity = jumper.getVelocity().projectedXZ().rotateAroundY(rollAmount*1);

        Vector currentVelocity = jumper.getVelocity();
        currentVelocity = currentVelocity.projectedXZ();

        Vector newAcc = targetVelocity.sub(currentVelocity);

        System.out.println("currentVelocity = " + currentVelocity);

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

    @Override
    public String toString() {
        return "Parachute";
    }
}
