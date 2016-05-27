import dat367.falling.core.FallingGame;
import dat367.falling.core.Jump;
import dat367.falling.core.LandedState;
import dat367.falling.core.NotificationManager;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResetTest {
    @Test
    public void resetTest(){
        FallingGame game = new FallingGame();
        Jump firstJump = game.getCurrentJump();
        assertTrue(firstJump != null);
        firstJump.getJumper().setFallState(new LandedState(null));

        NotificationManager.getDefault().registerEvent(FallingGame.SCREEN_DOUBLE_TAP_EVENT, null);

        assertFalse(firstJump == game.getCurrentJump());
    }
}