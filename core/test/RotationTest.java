import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RotationTest {
    @Test
    public void testRelativeTo() throws Exception {
        Rotation rotation = new Rotation(new Vector(0, -1, 0), new Vector(0, 0, 1));
        Rotation neutralRotation = new Rotation();
        Rotation relative = rotation.relativeTo(neutralRotation);
        Vector relDir = relative.getDirection();
        Vector relUp = relative.getUp();
        assertTrue(relDir.getX() == 0);
        assertTrue(relDir.getY() == 1);
        assertTrue(relDir.getZ() == 0);
        assertTrue(relUp.getX() == -1);
        assertTrue(relUp.getX() == 0);
        assertTrue(relUp.getX() == 0);
       }
}