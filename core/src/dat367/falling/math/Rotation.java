package dat367.falling.math;

/**
 * Defines an object's rotation in space using two vectors
 */
public class Rotation {
    private Vector direction;
    private Vector up;

    public Rotation(Vector direction, Vector up) {
        this.direction = direction;
        this.up = up;
    }

    public Vector getDirection() {
        return direction;
    }

    public Vector getUp() {
        return up;
    }


}
