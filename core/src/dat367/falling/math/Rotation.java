package dat367.falling.math;

/**
 * Defines an object's rotation in space using two vectors
 */
public class Rotation {
    private Matrix transformation;

    public Rotation(Vector direction, Vector up) {
        transformation = new Matrix(direction, up, direction.cross(up));
    }

    public Rotation(){
        transformation = new Matrix(1, 0, 0,
                                    0, 1, 0,
                                    0, 0, 1);
    }

    public Rotation(Matrix transformation) {
        this.transformation = transformation;
    }

    public Vector getDirection() {
        return transformation.getColumn1();
    }

    public Vector getUp() {
        return transformation.getColumn2();
    }

    public Vector getRight() {
        return transformation.getColumn3();
    }

    public Rotation rotate(Vector axis, float radians) {
        Matrix rotationMatrix = FallingMath.rotationMatrix(axis, radians);
        return new Rotation(rotationMatrix.mult(this.transformation)).normalized();
    }

    // Rotate by the given rotation relative to [direction = positive X axis, up = positive Y axis]
    public Rotation rotate(Rotation rotation) {
        return new Rotation(this.transformation.mult(rotation.transformation)).normalized();
    }

    public Rotation relativeTo(Rotation other){
        Matrix relative = this.transformation.transpose().mult(other.transformation);
        return new Rotation(relative);
    }

    public Rotation normalized() {
        return new Rotation(transformation.getColumn1().normalized(), transformation.getColumn2().normalized());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rotation rotation = (Rotation) o;

        return transformation != null ? transformation.equals(rotation.transformation) : rotation.transformation == null;

    }

    @Override
    public int hashCode() {
        return transformation != null ? transformation.hashCode() : 0;
    }

    @Override
    public String toString() {
        return transformation.toString();
    }

    public Matrix getRotationMatrix() {
        return transformation;
    }
}
