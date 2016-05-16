import dat367.falling.math.Matrix;
import dat367.falling.math.Vector;
import org.junit.Test;

import static org.junit.Assert.*;


public class MatrixTest {

    @Test
    public void testGetColumn1() throws Exception {
        Matrix a = new Matrix(1,2,3,
                             4,5,6,
                             7,8,9);

        Vector v = new Vector(1,4,7);
        assertTrue(a.getColumn1().equals(v));

    }

    @Test
    public void testGetColumn2() throws Exception {
        Matrix a = new Matrix(1,2,3,
                4,5,6,
                7,8,9);

        Vector v = new Vector(2,5,8);
        assertTrue(a.getColumn2().equals(v));
    }

    @Test
    public void testGetColumn3() throws Exception {
        Matrix a = new Matrix(1,2,3,
                4,5,6,
                7,8,9);

        Vector v = new Vector(3,6,9);
        assertTrue(a.getColumn3().equals(v));
    }

    @Test
    public void testScale() throws Exception {
        Matrix m = new Matrix(1,3,7,
                4,6,8,
                2,5,9);

        m = m.scale(2);

        Vector c1 = m.getColumn1();
        Vector c2 = m.getColumn2();
        Vector c3 = m.getColumn3();

        assertTrue(c1.equals(new Vector(2,8,4)));
        assertTrue(c2.equals(new Vector(6,12,10)));
        assertTrue(c3.equals(new Vector(14,16,18)));

    }

    @Test
    public void testAdd() throws Exception {
        Matrix m = new Matrix(1,2,3,
                4,5,6,
                7,8,9);
        Matrix a = new Matrix(2,3,4,
                5,6,7,
                8,9,10);

        m = m.add(a);

        assertTrue(m.getColumn1().equals(new Vector(3,9,15)));
        assertTrue(m.getColumn2().equals(new Vector(5,11,17)));
        assertTrue(m.getColumn3().equals(new Vector(7,13,19)));

    }

    @Test
    public void testMult() throws Exception {
        Matrix m = new Matrix(1,2,3,
                4,5,6,
                7,8,9);
        Vector v = new Vector(1,2,3);

        v = m.mult(v);

        assertTrue(v.equals(new Vector(14,32,50)));

    }
}