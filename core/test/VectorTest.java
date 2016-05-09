import dat367.falling.math.Matrix;
import dat367.falling.math.Vector;
import org.junit.Test;

import static org.junit.Assert.*;

public class VectorTest {

    @Test
    public void testAdd() throws Exception {
        Vector v = new Vector(1,1,1);
        Vector v2 = v.add(new Vector(2,3,4));
        assertTrue(v2.getX() == 3);
        assertTrue(v2.getY() == 4);
        assertTrue(v2.getZ() == 5);
        assertFalse(v==v2);
    }

    @Test
    public void testSub() throws Exception {
        Vector v = new Vector(10,10,10);
        Vector v2 = v.sub(new Vector(4,6,8));

        assertTrue(v2.getX() == 6);
        assertTrue(v2.getY() == 4);
        assertTrue(v2.getZ() == 2);

        assertFalse(v==v2);
    }

    @Test
    public void testLength() throws Exception {
        Vector v = new Vector(2,2,2);
        float vLength = v.length();

        assertTrue(vLength == (float)Math.sqrt(12));
    }

    @Test
    public void testLengthSquared() throws Exception {
        Vector v = new Vector(1,2,3);
        float lengthSquared = v.lengthSquared();

        assertTrue(lengthSquared == 1+4+9);
    }

    @Test
    public void testScale() throws Exception {
        Vector v = new Vector(1,2,3);
        Vector v2 = v.scale(2);

        assertTrue(v2.getX()==2);
        assertTrue(v2.getY()==4);
        assertTrue(v2.getZ()==6);

        assertFalse(v==v2);
    }

    @Test
    public void testNormalized() throws Exception {
        Vector v = new Vector(1,1,1);
        Vector v2 = v.normalized();

        assertEquals(1.0f, v2.length(), 0.0001f);

        assertFalse(v==v2);
    }

    @Test
    public void testProjectedXZ() throws Exception {
        Vector v = new Vector(1,1,1);
        Vector v2 = v.projectOntoPlaneXZ();

        assertTrue(v2.getX()==v.getX());
        assertTrue(v2.getY()==0);
        assertTrue(v2.getZ()==v.getZ());

        assertFalse(v==v2);
    }

    @Test
    public void testMirrorY() throws Exception {
        Vector v = new Vector(1,1,1);
        Vector v2 = v.mirrorPlaneXZ();

        assertTrue(v2.getX() == -v.getX());
        assertTrue(v2.getY() == v.getY());
        assertTrue(v2.getZ() == -v.getZ());

        assertFalse(v==v2);
    }

    @Test
    public void testDot() throws Exception {
        Vector v = new Vector(2,2,2);
        float f = v.dot(new Vector(3,3,3));

        assertTrue(f == 2*3+2*3+2*3);
    }

    @Test
    public void testCross() throws Exception {
        Vector v = new Vector(1,2,3);
        Vector v2 = new Vector(2,3,4);
        Vector v3 = v.cross(v2);

        assertTrue(v3.getX()==(8-9));
        assertTrue(v3.getY()==(6-4));
        assertTrue(v3.getZ()==(3-4));

        assertFalse(v==v2);
        assertFalse(v==v3);
        assertFalse(v2==v3);
    }

    @Test
    public void testMultWithMatrix() throws Exception {
        Vector v = new Vector(2,2,2);
        Matrix m = new Matrix(1,1,1,
                2,2,2,
                3,3,3);
        Vector vm = v.mul(m);

        assertTrue(vm.getX()==(2+2+2));
        assertTrue(vm.getY()==(4+4+4));
        assertTrue(vm.getZ()==(6+6+6));
        assertFalse(vm==v);
    }
}