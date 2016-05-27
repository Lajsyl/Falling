package dat367.falling.core;

import dat367.falling.math.Vector;

public interface IWorld {

    float AIR_DENSITY = 1.2041f * 5; // kg/m3 (at 20°C)

    // Defined according to the coordinate system used
    float GRAVITATION = -9.82f * 10;

    // Bright desaturated sky blue
    Vector ATMOSPHERE_COLOR = new Vector(165 / 255f, 215 / 255f, 250 / 255f);

    void update(float deltaTime);

    Jumper getJumper();

}
