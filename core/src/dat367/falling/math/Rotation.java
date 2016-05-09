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

    public Rotation rotate(Vector axis, float radians) {
        Matrix rotationMatrix = FallingMath.rotationMatrix(axis, radians);
        return new Rotation(rotationMatrix.mult(direction).normalized(), rotationMatrix.mult(up).normalized());
    }

    // Rotate this rotation by the given rotation relative to [direction = positive Z axis, up = positive Y axis]
    public Rotation rotate(Rotation rotation) {
        Vector right = direction.cross(up);
        return new Rotation(direction.scale(rotation.getDirection().getZ())
                .add(up.scale(rotation.getDirection().getY()))
                .add(right.scale(rotation.getDirection().getX())),
                            direction.scale(rotation.getUp().getZ())
                .add(up.scale(rotation.getUp().getY()))
                .add(right.scale(rotation.getUp().getX()))).normalized();
    }

    private Rotation normalized() {
        return new Rotation(direction.normalized(), up.normalized());
    }
//
}
