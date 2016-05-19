package dat367.falling.core;

import dat367.falling.math.Vector;

public class LandedState implements FallState {

    public static final String playerHasStopped = "playerHasStopped";

    @Override
    public void setup(Jumper jumper) {
        jumper.setAcceleration(new Vector(0, 0, 0));

        jumper.setVelocity(jumper.getVelocity().projectOntoPlaneXZ());

        Vector position = jumper.getPosition();
        jumper.setPosition(position.getX(), Jumper.BODY_HEIGHT, position.getZ());
    }

    // TODO: Might create a change in position as to mimic the shock when landing
    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        jumper.setVelocity(jumper.getVelocity().scale(0.98f));
        jumper.setPosition(jumper.getPosition().add(jumper.getVelocity().scale(deltaTime)));

        if(jumper.getVelocity().lengthSquared() < 0.05f){
            jumper.setVelocity(0, 0 , 0);
            NotificationManager.registerEvent(playerHasStopped, null);
        }

        return null;
    }

    @Override
    public String toString() {
        return "Landed";
    }

}
