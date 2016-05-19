import dat367.falling.math.Matrix;
import dat367.falling.math.Vector;
import org.junit.Test;

import static org.junit.Assert.*;


public class MatrixTest {

    @Test
    public void testGetRow1() throws Exception {
        Matrix a = new Matrix(1,2,3,
                             4,5,6,
                             7,8,9);

        Vector v = new Vector(1,2,3);
        assertTrue(a.getRow1().equals(v));

    }

    @Test
    public void testGetRow2() throws Exception {
        Matrix a = new Matrix(1,2,3,
                4,5,6,
                7,8,9);

        Vector v = new Vector(4,5,6);
        assertTrue(a.getRow2().equals(v));
    }

    @Test
    public void testGetRow3() throws Exception {
        Matrix a = new Matrix(1,2,3,
                4,5,6,
                7,8,9);

        Vector v = new Vector(7,8,9);
        assertTrue(a.getRow3().equals(v));
    }

    @Test
    public void testScale() throws Exception {
        Matrix m = new Matrix(1,3,7,
                4,6,8,
                2,5,9);

        m = m.scale(2);

        Vector c1 = m.getRow1();
        Vector c2 = m.getRow2();
        Vector c3 = m.getRow3();

        assertTrue(c1.equals(new Vector(2,6,14)));
        assertTrue(c2.equals(new Vector(8,12,16)));
        assertTrue(c3.equals(new Vector(4,10,18)));

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

        assertTrue(m.getRow1().equals(new Vector(3,5,7)));
        assertTrue(m.getRow2().equals(new Vector(9,11,13)));
        assertTrue(m.getRow3().equals(new Vector(15,17,19)));

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