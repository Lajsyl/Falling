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

    public static float smoothstep(float edge0, float edge1, float x) {
        // From https://www.opengl.org/sdk/docs/man/html/smoothstep.xhtml
        float t = clamp0_1((x - edge0) / (edge1 - edge0));
        return t * t * (3.0f - 2.0f * t);
    }

}
