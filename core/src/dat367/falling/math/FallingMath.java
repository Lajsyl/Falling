package dat367.falling.math;

public class FallingMath {

    public static final Matrix identityMatrix = new Matrix(1, 0, 0,
                                                           0, 1, 0,
                                                           0, 0, 1);

    public static double clamp(double x, double min, double max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float clamp(float x, float min, float max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float clamp01(float x) {
        return clamp(x, 0.0f, 1.0f);
    }

    public static double interpolateSmooth(double x) {
        return (Math.sin((x - 0.5) * Math.PI) + 1.0) / 2;
    }

    public static float smoothstep(float edge0, float edge1, float x) {
        // From https://www.opengl.org/sdk/docs/man/html/smoothstep.xhtml
        float t = clamp01((x - edge0) / (edge1 - edge0));
        return t * t * (3.0f - 2.0f * t);
    }

    // Rodrigues' rotation formula
    public static Matrix rotationMatrix(Vector axis, float radians) {
        float sin = (float)Math.sin(radians);
        float cos = (float)Math.cos(radians);
        float oneMinusCos = 1 - cos;
        return new Matrix(cos + axis.getX()*axis.getX()*oneMinusCos, axis.getX()*axis.getY()*oneMinusCos - axis.getZ()*sin, axis.getX()*axis.getZ()*oneMinusCos + axis.getY()*sin,
                          axis.getY()*axis.getX()*oneMinusCos + axis.getZ()*sin, cos + axis.getY()*axis.getY()*oneMinusCos, axis.getY()*axis.getZ()*oneMinusCos - axis.getX()*sin,
                axis.getZ()*axis.getX()*oneMinusCos - axis.getY()*sin, axis.getZ()*axis.getY()*oneMinusCos + axis.getX()*sin, cos + axis.getZ()*axis.getZ()*oneMinusCos);
    }

}
