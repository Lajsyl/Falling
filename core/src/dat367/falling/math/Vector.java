package dat367.falling.math;

import java.text.DecimalFormat;

public class Vector {

    private final float x;
    private final float y;
    private final float z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector other) {
        this(other.getX(), other.getY(), other.getZ());
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

    public float dot(Vector other) {
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    public Vector cross(Vector other) {
        return new Vector(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    public Vector mul(Matrix matrix) {
        return new Vector(
                matrix.getColumn1().dot(this),
                matrix.getColumn2().dot(this),
                matrix.getColumn3().dot(this)
        );
    }

    public Vector scale(float scale) {
        return new Vector(x * scale, y * scale, z * scale);
    }

    public float length() {
        return (float)Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return x*x + y*y + z*z;
    }

    public Vector normalized() {
        float length = length();
        return scale(1.0f / length);
    }

    public Vector projectOntoLine(Vector line) {
        Vector direction = line.normalized();
        float length = this.dot(line);
        return direction.scale(length);
    }

    public Vector projectOntoPlaneXZ() {
        return new Vector(this.getX(), 0, this.getZ());
    }

    public Vector mirrorPlaneXZ() {
        return new Vector(-getX(), getY(), -getZ());
    }

    public Vector rotateAroundY(float radians) {
        return this.mul(
                new Matrix(
                        (float) Math.cos(radians), 0, (float)Math.sin(radians),
                        0, 1, 0,
                        (float)-Math.sin(radians), 0, (float) Math.cos(radians)
                )
        );
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
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        return "(" + df.format(x) + ", " + df.format(y) + ", " + df.format(z) + ")";
    }

}
