package dat367.falling.core;

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

        //For Y-acc look att the parachute. Should be the same.
        return null;
    }

    public boolean isPosYZero(Jumper jumper){
     return false;
    }
}
