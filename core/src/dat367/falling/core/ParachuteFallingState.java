package dat367.falling.core;


import dat367.falling.core.world.World;
import dat367.falling.math.FallingMath;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Model;


// TODO MAKE FUNCTIONING ON PC
// TODO Make falling faster when turning (decrease air resistance)

public class ParachuteFallingState implements FallState {

    private float rotationalSpeed;
    private float rotationalAcceleration;

//    private Model parachute = new Model("parachute.g3db");

    private float forwardSpeed = 30;

    @Override
    public void setup(Jumper jumper) {
        jumper.setBodyRotation(new Rotation(jumper.getLookDirection().projectOntoPlaneXZ().normalized(), new Vector(0, 1, 0)));
        jumper.setVelocity(jumper.getVelocity().add(jumper.getBodyRotation().getDirection().scale(10)));

    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        System.out.println("handle Falling");
        jumper.setAcceleration(calculateAcceleration(jumper));

        Vector v0 = jumper.getVelocity();

        rotationalAcceleration = calculateRotationalAcceleration(deltaTime, jumper);
        rotationalSpeed = calculcateRotationalSpeed(deltaTime);
        jumper.setBodyRotation(new Rotation(jumper.getBodyRotation().getDirection().rotateAroundY(rotationalSpeed), jumper.getBodyRotation().getUp()));

        Vector velocity = calculateVelocity(deltaTime, jumper);//.rotateAroundY(rotationalSpeed);

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
//        Vector targetVelocity = jumper.getVelocity().projectOntoPlaneXZ().rotateAroundY(rotationalSpeed);
//
//
//        Vector currentVelocity = jumper.getVelocity();
//        currentVelocity = currentVelocity.projectOntoPlaneXZ();
//
//        Vector newAcc = targetVelocity.sub(currentVelocity);
//
//        System.out.println("currentVelocity = " + currentVelocity);
//
//        return newAcc.scale(50f);
//
//    }

    private Vector calculateVelocity(float deltaTime, Jumper jumper){
        System.out.println(jumper.getBodyRotation().getDirection());
        return new Vector(0, jumper.getVelocity().getY(), 0).add(jumper.getAcceleration().scale(deltaTime))
                .add(jumper.getBodyRotation().getDirection().scale(forwardSpeed));
    }

    private float calculateRotationalAcceleration(float deltaTime, Jumper jumper) {
        Vector up = new Vector(0, 1, 0);

        Vector rightDirection = jumper.getBodyRotation().getDirection().projectOntoPlaneXZ().normalized().cross(up);

        Vector projected = jumper.getHeadRotation().getUp().projectOntoLine(rightDirection);

        Float rollAmount = rightDirection.sub(projected).length() - 1;
        System.out.println("rollAmount = " + rollAmount);

        float maxRollAmount = 0.5f;
        rollAmount = FallingMath.clamp(rollAmount, -maxRollAmount, maxRollAmount);
        rollAmount = rollAmount / maxRollAmount;

        float targetRotationalSpeed = rollAmount * 0.03f;

        return (targetRotationalSpeed - rotationalSpeed) * 2f;
    }

    private float calculcateRotationalSpeed(float deltaTime){
        return rotationalSpeed += rotationalAcceleration * deltaTime;
    }

    private Vector calculatePosition(float deltaTime, Jumper jumper, Vector v0){

        System.out.println("position: " + jumper.getPosition());
        return jumper.getPosition().add(jumper.getVelocity().add(v0).scale(deltaTime/2));
    }

    @Override
    public String toString() {
        return "Parachute";
    }
}
