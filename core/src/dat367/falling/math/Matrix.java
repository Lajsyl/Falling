package dat367.falling.math;

public class Matrix {

    private float i1j1, i1j2, i1j3;
    private float i2j1, i2j2, i2j3;
    private float i3j1, i3j2, i3j3;

    public Matrix(float i1j1, float i1j2, float i1j3,
            float i2j1, float i2j2, float i2j3,
            float i3j1, float i3j2, float i3j3) {
        this.i1j1 = i1j1; this.i1j2 = i1j2; this.i1j3 = i1j3;
        this.i2j1 = i2j1; this.i2j2 = i2j2; this.i2j3 = i2j3;
        this.i3j1 = i3j1; this.i3j2 = i3j2; this.i3j3 = i3j3;
    }

    public Matrix(Vector col1, Vector col2, Vector col3) {
        i1j1 = col1.getX(); i1j2 = col2.getX(); i1j3 = col3.getX();
        i2j1 = col1.getY(); i2j2 = col2.getY(); i2j3 = col3.getY();
        i3j1 = col1.getZ(); i3j2 = col2.getZ(); i3j3 = col3.getZ();
    }

    public Vector getColumn1() {
        return new Vector(i1j1, i2j1, i3j1);
    }

    public Vector getColumn2() {
        return new Vector(i1j2, i2j2, i3j2);
    }

    public Vector getColumn3() {
        return new Vector(i1j3, i2j3, i3j3);
    }

    public Vector getRow1(){
        return new Vector(this.i1j1, this.i1j2, this.i1j3);
    }

    public Vector getRow2(){
        return new Vector(this.i2j1, this.i2j2, this.i2j3);
    }

    public Vector getRow3(){
        return new Vector(this.i3j1, this.i3j2, this.i3j3);
    }

    public Matrix scale(float scale) {
        return new Matrix(i1j1*scale, i1j2*scale, i1j3*scale,
                          i2j1*scale, i2j2*scale, i2j3*scale,
                          i3j1*scale, i3j2*scale, i3j3*scale);
    }

    public Matrix add(Matrix other) {
        return new Matrix(i1j1+other.i1j1, i1j2+other.i1j2, i1j3+other.i1j3,
                          i2j1+other.i2j1, i2j2+other.i2j2, i2j3+other.i2j3,
                          i3j1+other.i3j1, i3j2+other.i3j2, i3j3+other.i3j3);
    }

    public Vector mult(Vector vector) {
        return new Vector(i1j1*vector.getX() + i1j2*vector.getY() + i1j3*vector.getZ(),
                          i2j1*vector.getX() + i2j2*vector.getY() + i2j3*vector.getZ(),
                          i3j1*vector.getX() + i3j2*vector.getY() + i3j3*vector.getZ());
    }

    public Matrix mult(Matrix other) {
        return new Matrix(this.mult(other.getColumn1()), this.mult(other.getColumn2()), this.mult(other.getColumn3()));
    }

    public Matrix transpose() {
        return new Matrix(i1j1, i2j1, i3j1,
                          i1j2, i2j2, i3j2,
                          i1j3, i2j3, i3j3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix = (Matrix) o;

        if (matrix.i1j1 != i1j1) return false;
        if (matrix.i1j2 != i1j2) return false;
        if (matrix.i1j3 != i1j3) return false;
        if (matrix.i2j1 != i2j1) return false;
        if (matrix.i2j2 != i2j2) return false;
        if (matrix.i2j3 != i2j3) return false;
        if (matrix.i3j1 != i3j1) return false;
        if (matrix.i3j2 != i3j2) return false;
        return (matrix.i3j3 == i3j3);

    }

    @Override
    public int hashCode() {
        int result = (i1j1 != +0.0f ? Float.floatToIntBits(i1j1) : 0);
        result = 31 * result + (i1j2 != +0.0f ? Float.floatToIntBits(i1j2) : 0);
        result = 31 * result + (i1j3 != +0.0f ? Float.floatToIntBits(i1j3) : 0);
        result = 31 * result + (i2j1 != +0.0f ? Float.floatToIntBits(i2j1) : 0);
        result = 31 * result + (i2j2 != +0.0f ? Float.floatToIntBits(i2j2) : 0);
        result = 31 * result + (i2j3 != +0.0f ? Float.floatToIntBits(i2j3) : 0);
        result = 31 * result + (i3j1 != +0.0f ? Float.floatToIntBits(i3j1) : 0);
        result = 31 * result + (i3j2 != +0.0f ? Float.floatToIntBits(i3j2) : 0);
        result = 31 * result + (i3j3 != +0.0f ? Float.floatToIntBits(i3j3) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "i1j1=" + i1j1 +
                ", i1j2=" + i1j2 +
                ", i1j3=" + i1j3 +
                ", i2j1=" + i2j1 +
                ", i2j2=" + i2j2 +
                ", i2j3=" + i2j3 +
                ", i3j1=" + i3j1 +
                ", i3j2=" + i3j2 +
                ", i3j3=" + i3j3 +
                '}';
    }
}
