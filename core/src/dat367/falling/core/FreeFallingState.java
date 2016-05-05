package dat367.falling.core;

import dat367.falling.core.world.World;
import dat367.falling.math.Vector;

import java.util.Observable;
import java.util.Observer;

public class FreeFallingState implements FallState, Observer {

    private boolean parachutePulled = false;


    @Override
    public void setup(Jumper jumper) {
        jumper.addObserver(this);
    }

    @Override // from Observer
    public void update(Observable o, Object arg) {
        if (o instanceof Jumper) {
            Jumper jumper = (Jumper) o;
            parachutePulled = jumper.getScreenClicked();
        }
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        // Save velocity for previous frame for later calculations
        Vector previousFrameVelocity = new Vector(jumper.getVelocity());

        jumper.setAcceleration(calculateAcceleration(jumper, deltaTime));
        jumper.setVelocity(calculateVelocity(deltaTime, jumper));
        jumper.setPosition(calculatePosition(deltaTime, jumper, previousFrameVelocity));

        if (parachutePulled){
            return new ParachuteFallingState();
        }

        return null;
    }

    private Vector calculateAcceleration(Jumper jumper, float deltaTime) {
        return calculateAccelerationY(jumper, deltaTime).add(calculateAccelerationXZ(jumper, deltaTime));
    }

    private Vector calculateAccelerationY(Jumper jumper, float deltaTime) {
        final float AIR_DENSITY = 1.2041f; // kg/m3 (at 20°C)
        float yVelocitySquared = (float) Math.pow(jumper.getVelocity().getY(), 2);

/*
        // Calculate drag
        float dragForce = 0.5f * AIR_DENSITY * yVelocitySquared * Jumper.AREA * 1.70f;

        // Calculate gravitational force
        float gravitationalForce = World.GRAVITATION * Jumper.MASS;

        float combinedForce = gravitationalForce + dragForce;

        float acceleration = combinedForce / Jumper.MASS * deltaTime;
        return new Vector(0, acceleration, 0);
*/

        float drag = 0.5f * AIR_DENSITY * yVelocitySquared * Jumper.AREA * 0.70f;
        float newY = (World.GRAVITATION * 90 + drag) / 90;
        return new Vector(0, newY, 0);
    }

    private Vector calculateAccelerationXZ(Jumper jumper, float deltaTime) {
        // Calculate target velocity
        Vector targetVelocity = jumper.getLookDirection().normalized();
        targetVelocity = targetVelocity.projectOntoPlaneXZ().scale(80);

        // Clamp speed
        float maxSpeed = 35f;
        if (targetVelocity.length() > maxSpeed) {
            targetVelocity = targetVelocity.normalized().scale(maxSpeed);
        }

        // Calculate acceleration from target speed
        Vector currentVelocity = jumper.getVelocity().projectOntoPlaneXZ();
        Vector newAcceleration = targetVelocity.sub(currentVelocity);
        return newAcceleration.scale(10);
    }

    
    private Vector calculateVelocity(float deltaTime, Jumper jumper) {
        return jumper.getVelocity().add(jumper.getAcceleration().scale(deltaTime));
    }

    private Vector calculatePosition(float deltaTime, Jumper jumper, Vector v0){
        Vector averageFrameAcceleration = jumper.getAcceleration().add(v0).scale(1.0f / 2.0f);
        return jumper.getPosition().add(averageFrameAcceleration.scale(deltaTime));
    }

    @Override
    public String toString() {
        return "Free falling";
    }
}
