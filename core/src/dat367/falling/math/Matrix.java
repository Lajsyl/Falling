package dat367.falling.math;

public class Matrix {

    float i1j1, i1j2, i1j3,
            i2j1, i2j2, i2j3,
            i3j1, i3j2, i3j3;

    public Matrix(float i1j1, float i1j2, float i1j3,
                  float i2j1, float i2j2, float i2j3,
                  float i3j1, float i3j2, float i3j3 ){

        this.i1j1 = i1j1; this.i1j2 = i1j2; this.i1j3 = i1j3;
        this.i2j1 = i2j1; this.i2j2 = i2j2; this.i2j3 = i2j3;
        this.i3j1 = i3j1; this.i3j2 = i3j2; this.i3j3 = i3j3;

    }

    public Vector getI1(){
        return new Vector(this.i1j1, this.i1j2, this.i1j3);
    }

    public Vector getI2(){
        return new Vector(this.i2j1, this.i1j2, this.i2j3);
    }

    public Vector getI3(){
        return new Vector(this.i3j1, this.i3j2, this.i3j3);
    }

}
