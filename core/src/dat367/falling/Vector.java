package dat367.falling;

public class Vector {

    private float x, y, z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public float dot(Vector other) {
        return x*other.x + y*other.y + z*other.z;
    }

    public Vector cross(Vector other) {
        return new Vector(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
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
        int result = Float.hashCode(x);
        result = 31*result + Float.hashCode(y);
        result = 31*result + Float.hashCode(z);
        return result;
    }

}
