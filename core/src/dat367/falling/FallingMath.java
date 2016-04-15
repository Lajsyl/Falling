package dat367.falling;

public class FallingMath {

    public static float clamp(float x, float min, float max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float clamp0_1(float x) {
        return clamp(x, 0.0f, 1.0f);
    }

}
