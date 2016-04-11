package dat367.falling;

import com.badlogic.gdx.math.Vector3;

public class Vector {

    private float x, y, z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    float getZ() {
        return z;
    }

    Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    Vector sub(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    float length() {
        return (float)Math.sqrt(lengthSquared());
    }

    float lengthSquared() {
        return x*x + y*y + z*z;
    }

    float dot(Vector other) {
        return x*other.x + y*other.y + z*other.z;
    }

    Vector3 asLibGdxVector() {
        return new Vector3(x, y, z);
    }

}
