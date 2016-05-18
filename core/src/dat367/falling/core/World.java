package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;
import dat367.falling.platform_abstraction.Sound;

public class World {

    public static final float AIR_DENSITY = 1.2041f; // kg/m3 (at 20°C)

    // Defined according to the coordinate system used
    public static final float GRAVITATION = -9.82f;

    private Ground ground;
    private CloudSimulator cloudSimulator;
    private Jumper jumper;
    private Airplane airplane;
    private Sound airplaneWindSound = new Sound("wind01.wav");
    private Sound airplaneDoorWindSound = new Sound("wind02.wav");
    private PositionedSound airplaneWind;
    private PositionedSound airplaneDoorWind;

    public World(ResourceRequirements resourceRequirements) {
        CollisionManager.clear();

        airplane = new Airplane(resourceRequirements, new Vector(0, 4000, 0));
        ground = new Ground(resourceRequirements);

        // Create jumper using the airplane metrics
        jumper = new Jumper(resourceRequirements, airplane.getHeadStartPosition(), airplane.getLookOutDirection());

        cloudSimulator = new CloudSimulator(resourceRequirements, jumper);

        resourceRequirements.require(airplaneWindSound);
//        airplaneWind = new PositionedSound(airplaneWindSound, airplane.getPosition().add(new Vector(0, 0, -2)));
        airplaneWind = new PositionedSound(airplaneWindSound, airplane.getHeadStartPosition().add(new Vector(2, 0, 0)));

        // (1, 0, 0) = positiv z-axel
        // (0, 1, 0) = positiv y-axel
        // (0, 0, 1) = positiv x-axel

        airplaneWind.loop();
//        resourceRequirements.require(airplaneDoorWindSound);
//        airplaneDoorWind = new PositionedSound(airplaneDoorWindSound, airplane.getPosition().add(new Vector(0, 0, -2)));
//        airplaneDoorWind.loop();
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        ground.update(deltaTime);
        cloudSimulator.update(deltaTime, jumper, airplane);
        airplane.update(deltaTime);

        CollisionManager.update(deltaTime);
    }

    public Jumper getJumper() {
        return jumper;
    }
}
