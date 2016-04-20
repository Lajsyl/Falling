package dat367.falling.math;

public class FallingMath {

    public static float clamp(float x, float min, float max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float clamp0_1(float x) {
        return clamp(x, 0.0f, 1.0f);
    }

    public static double clamp(double x, double min, double max) {
        return Math.max(min, Math.min(x, max));
    }

    public static double interpolateSmooth(double x) {
        return (Math.sin((x - 0.5) * Math.PI) + 1.0) / 2;
    }

}
