package dat367.falling.core;


import dat367.falling.core.world.World;
import dat367.falling.math.Vector;


public class ParachuteFallingState implements FallState {

    private Vector parachuteDirection;

    private float rotationalSpeed;
    private float rotationalAcceleration;


    @Override
    public void setup(Jumper jumper) {
        jumper.setNeutralDirection(jumper.getLookDirection().projectedXZ().normalized());
        setParachuteDirection(jumper.getNeutralDirection());
        jumper.setVelocity(jumper.getVelocity().add(jumper.getNeutralDirection().scale(10)));

    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        System.out.println("handle Falling");
        jumper.setAcceleration(calculateAcceleration(jumper));

        Vector v0 = jumper.getVelocity();

        rotationalAcceleration = calculateRotationalAcceleration(deltaTime, jumper);
        rotationalSpeed = calculcateRotationalSpeed(deltaTime);

        Vector velocity = calculateVelocity(deltaTime, jumper).rotateAroundY(rotationalSpeed);

        jumper.setVelocity(velocity);
        jumper.setPosition(calculatePosition(deltaTime, jumper, v0));


        if (jumper.getPosition().getY() < 1){
            return new LandedState();
        }

        return null;
    }

    private Vector calcAccXZ(Jumper jumper) {
        return new Vector(0, 0, 0);
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
//    private Vector rotateParachute(float deltaTime, Jumper jumper){
//
//        float rotationalSpeed = calculcateRotationalSpeed(deltaTime);
//
//        Vector targetVelocity = jumper.getVelocity().projectedXZ().rotateAroundY(rotationalSpeed);
//
//
//        Vector currentVelocity = jumper.getVelocity();
//        currentVelocity = currentVelocity.projectedXZ();
//
//        Vector newAcc = targetVelocity.sub(currentVelocity);
//
//        System.out.println("currentVelocity = " + currentVelocity);
//
//        return newAcc.scale(50f);
//
//    }

    private Vector calculateVelocity(float deltaTime, Jumper jumper){

        return new Vector(jumper.getVelocity().add(jumper.getAcceleration().scale(deltaTime)));
    }

    private float calculateRotationalAcceleration(float deltaTime, Jumper jumper) {
        Vector up = new Vector(0, 1, 0);

        Vector rightDirection = jumper.getVelocity().projectedXZ().normalized().cross(up);

        Vector projected = jumper.getUpVector().lineProjection(rightDirection);

        Float rollAmount = rightDirection.sub(projected).length() - 1;
        System.out.println("rollAmount = " + rollAmount);

        float targetRotationalSpeed = rollAmount * 0.1f;

        return (targetRotationalSpeed - rotationalSpeed) * 5f;
    }

    private float calculcateRotationalSpeed(float deltaTime){
        return rotationalSpeed += rotationalAcceleration * deltaTime;
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
