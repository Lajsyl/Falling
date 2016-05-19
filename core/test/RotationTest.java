import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


public class RotationTest {

    @Test
    public void testGetDirection() throws Exception {
        Rotation r = new Rotation(new Vector(1,17,3), new Vector(2,16,78));

        assertTrue(r.getDirection().equals(new Vector (1,17,3)));
    }

    @Test
    public void testGetUp() throws Exception {
        Rotation r = new Rotation(new Vector(1,17,3), new Vector(2,16,78));

        assertTrue(r.getUp().equals(new Vector (2,16,78)));
    }

    @Test
    public void testGetRight() throws Exception {
        Rotation r = new Rotation(new Vector(1,0,0), new Vector(0,1,0));

        assertTrue(r.getRight().equals(new Vector(0,0,1)));
    }

    @Ignore
    @Test
    public void testRotate() throws Exception {
        Rotation r = new Rotation(new Vector(1,0,0), new Vector(0,1,0));

        r.rotate(new Vector(0,1,0), (float)Math.PI/2);

        //TODO account for inexactness caused by rounding errors
        assertTrue(r.equals(new Rotation(new Vector(0,0,-1), new Vector(0,1,0))));
    }

    @Ignore
    @Test
    public void testRotate1() throws Exception {
        Rotation r = new Rotation(new Vector(1,0,0), new Vector(0,1,0));
        r.rotate(new Rotation(new Vector(0,1,0), new Vector(0,0,1)));

        //TODO how is this even supposed to work?
        assertTrue(r.equals(new Rotation(new Vector(0,1,0), new Vector(0,0,1))));

    }

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