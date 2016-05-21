import dat367.falling.core.Jumper;
import dat367.falling.core.LandedState;
import dat367.falling.core.ParachuteFallingState;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LandedStateTest {
    @Test
    public void stopWhenLanded(){
        Jumper jumper = new Jumper(new ResourceRequirements(), new Vector(0,1,0), new Rotation(new Vector(0,0,0), new Vector(0,0,0)));
        jumper.setFallState(new LandedState());
        jumper.setVelocity(10,10,10);
        jumper.getFallState().setup(jumper);
        float startY = jumper.getPosition().getY();
        assertFalse(jumper.getVelocity().length() == 0);
        assertTrue(jumper.getVelocity().getY() == 0);
        assertTrue(startY == jumper.getPosition().getY());
        while(jumper.getVelocity().length() != 0){
            jumper.getFallState().handleFalling(0.0016f, jumper);
        }
        assertTrue(true);
    }
}