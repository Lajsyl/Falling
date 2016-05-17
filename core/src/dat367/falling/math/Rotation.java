package dat367.falling.math;

/**
 * Defines an object's rotation in space using two vectors
 */
public class Rotation {
    private Matrix transformation;

//    private Vector direction;
//    private Vector up;

    public Rotation(Vector direction, Vector up) {
        transformation = new Matrix(direction, up, direction.cross(up));
//        this.direction = direction;
//        this.up = up;
    }

    public Rotation(){
        transformation = new Matrix(1, 0, 0,
                                    0, 1, 0,
                                    0, 0, 1);

//        this.direction = new Vector(0,0,1);
//        this.up = new Vector(0,1,0);
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
        return new Rotation(rotationMatrix.mult(direction).normalized(), rotationMatrix.mult(up).normalized());
    }

    // Rotate this rotation by the given rotation relative to [direction = positive X axis, up = positive Y axis]
    public Rotation rotate(Rotation rotation) {

//        Vector right = direction.cross(up);
//        return new Rotation(direction.scale(rotation.getDirection().getZ())
//                .add(up.scale(rotation.getDirection().getY()))
//                .add(right.scale(rotation.getDirection().getX())),
//                            direction.scale(rotation.getUp().getZ())
//                .add(up.scale(rotation.getUp().getY()))
//                .add(right.scale(rotation.getUp().getX()))).normalized();
    }

    private Rotation normalized() {
        return new Rotation(direction.normalized(), up.normalized());
    }

    public Rotation relativeTo(Rotation other){
        Vector up = new Vector(-other.getUp().projectOntoLine(this.getRight()).length(), other.getUp().projectOntoLine(this.getUp()).length(), other.getUp().projectOntoLine(this.getDirection()).length());
        Vector forward = new Vector(-other.getDirection().projectOntoLine(this.getRight()).length(), other.getDirection().projectOntoLine(this.getUp()).length(), other.getDirection().projectOntoLine(this.getDirection()).length());
        Vector right = new Vector(-other.getRight().projectOntoLine(this.getRight()).length(), other.getRight().projectOntoLine(this.getUp()).length(), other.getRight().projectOntoLine(this.getDirection()).length());

        System.out.println(forward);
        System.out.println(up);
        System.out.println(right);
        return new Rotation(forward,up);
    }

}
