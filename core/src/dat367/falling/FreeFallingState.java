package dat367.falling;

public class FreeFallingState implements FallState {
    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {


        jumper.setAcceleration(jumper.getAcceleration().getX(), jumper.getAcceleration().getY(), calculateAcceleration(jumper));
        jumper.setVelocity(jumper.getVelocity().getX(), jumper.getVelocity().getY(), calculateVelocity(deltaTime, jumper));

        return null;
    }

    private float calculateAcceleration(Jumper jumper){
        float drag = (float)(0.5*1.3*1.2041*0.85)*jumper.getAcceleration().getZ();
        return (World.GRAVITATION*80 - drag)/80;
    }
    
    private float calculateVelocity(float deltaTime, Jumper jumper){
        return jumper.getVelocity().getZ() + jumper.getAcceleration().getZ()*deltaTime;
    }
}
