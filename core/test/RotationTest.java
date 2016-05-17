import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RotationTest {
    @Test
    public void testRelativeTo() throws Exception {
        Rotation neutral = new Rotation();
        Rotation right = new Rotation(new Vector(0, 0, 1), new Vector(0, 1, 0));
        Rotation left = new Rotation(new Vector(0, 0, -1), new Vector(0, 1, 0));
        assertTrue(neutral.relativeTo(right).equals(right));
        System.out.println(right.relativeTo(neutral));
        System.out.println(left);
        System.out.println(right.relativeTo(neutral).equals(left));
        assertTrue(right.relativeTo(neutral).equals(left));
       }
}