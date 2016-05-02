import dat367.falling.core.Jumper;
import dat367.falling.core.LandedState;
import dat367.falling.math.Vector;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LandedStateTest {

    @Test
    public void handleFallingTest(){
        Jumper jumper = new Jumper(new Vector(0,100,0), new Vector(0,0,0));
        //jumper.setFallState(new ParachuteState());
        while(true){
            jumper.getFallState().handleFalling(0.0016f, jumper);
            if(jumper.getFallState() instanceof LandedState){
                jumper.getFallState().handleFalling(0.0016f, jumper);
                assertTrue(jumper.getVelocity().getY() == 0);
                while(true){
                    jumper.getFallState().handleFalling(0.0016f, jumper);

                    if(jumper.getVelocity().getX() == 0 && jumper.getVelocity().getZ() == 0) {
                        break;
                    }
                }
                break;
            }
        }
        assertTrue(true);

    }
}