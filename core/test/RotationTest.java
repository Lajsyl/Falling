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


    @Test
    public void testRotate() throws Exception {
        Rotation r = new Rotation(new Vector(1,0,0), new Vector(0,1,0));

        Rotation actual = r.rotate(new Vector(0,1,0), (float)Math.PI/2);


        Rotation expected = new Rotation(new Vector(0,0,-1), new Vector(0,1,0));


        assertEquals(expected.getDirection().getX(), actual.getDirection().getX(), 0.0000001f);
        assertEquals(expected.getDirection().getY(), actual.getDirection().getY(), 0.0000001f);
        assertEquals(expected.getDirection().getZ(), actual.getDirection().getZ(), 0.0000001f);

        assertEquals(expected.getUp().getX(), actual.getUp().getX(), 0.0000001f);
        assertEquals(expected.getUp().getY(), actual.getUp().getY(), 0.0000001f);
        assertEquals(expected.getUp().getZ(), actual.getUp().getZ(), 0.0000001f);
    }

    @Test
    public void testRotate1() throws Exception {
        Rotation r = new Rotation(new Vector(1,0,0), new Vector(0,1,0));
        Rotation actual = r.rotate(new Rotation(new Vector(0,0,-1), new Vector(0,1,0)));

        Rotation expected = new Rotation(new Vector(0,0,-1), new Vector(0,1,0));


        assertEquals(expected.getDirection().getX(), actual.getDirection().getX(), 0.0000001f);
        assertEquals(expected.getDirection().getY(), actual.getDirection().getY(), 0.0000001f);
        assertEquals(expected.getDirection().getZ(), actual.getDirection().getZ(), 0.0000001f);

        assertEquals(expected.getUp().getX(), actual.getUp().getX(), 0.0000001f);
        assertEquals(expected.getUp().getY(), actual.getUp().getY(), 0.0000001f);
        assertEquals(expected.getUp().getZ(), actual.getUp().getZ(), 0.0000001f);

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