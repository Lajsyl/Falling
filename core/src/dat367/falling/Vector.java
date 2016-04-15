package dat367.falling;

public class Vector {

    private float x, y, z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector(Vector v){
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Vector sub(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public float length() {
        return (float)Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return x*x + y*y + z*z;
    }

    public Vector normalized() {
        float length = length();
        return new Vector(x / length, y / length, z / length);
    }

    public Vector projectedXZ(){

        return new Vector(this.multWithMatrix(new Matrix(1,0,0,
                                                        0,0,0,
                                                        0,0,1)));


    }

    public Vector mirrorY(){

        return new Vector(this.multWithMatrix(new Matrix(-1,0,0,
                                                        0,1,0,
                                                        0,0,-1)));
    }

    public Vector scale(float factor){
        return new Vector(this.getX()*factor, this.getY()*factor, this.getZ()*factor);
    }

    public float dot(Vector other) {
        return x*other.x + y*other.y + z*other.z;
    }

    public Vector cross(Vector other) {
        return new Vector(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
    }

    public Vector multWithMatrix(Matrix A){
        return new Vector(A.getI1().dot(this), A.getI2().dot(this), A.getI3().dot(this));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() == this.getClass()) {
            Vector v = (Vector) obj;
            if (x == v.x && y == v.y && z == v.z) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = Float.valueOf(x).hashCode();
        result = 31*result + Float.valueOf(y).hashCode();
        result = 31*result + Float.valueOf(z).hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + Math.round(x) + ", " + Math.round(y) + ", " + Math.round(z) + ")";
    }

}
