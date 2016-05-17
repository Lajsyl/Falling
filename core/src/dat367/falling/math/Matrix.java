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

}
