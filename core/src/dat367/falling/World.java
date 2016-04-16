package dat367.falling;

public class World {
    private Ground ground;

    //defined according to the coordinate system used
    public static final float GRAVITATION = -9.82f;

    public void update(float deltaTime) {

    }

    public Vector getStartPosition() {
        return new Vector(0f, 0.5f, -1.8f);
    }

    public Vector getStartLookDirection() {
//        return new Vector(0.975f, -0.206f, -0.070f);
        return new Vector(0.975f, 0, -0.070f).normalized();
    }
}
