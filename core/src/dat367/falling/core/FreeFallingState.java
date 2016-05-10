package dat367.falling.core;

import dat367.falling.math.FallingMath;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;

import java.util.Observable;
import java.util.Observer;

public class FreeFallingState implements FallState, Observer {

    private boolean parachutePulled = false;

    public static final float XZ_ACCELERATION_MULTIPLIER =  1.0f;
    public static final float Y_ACCELERATION_MULTIPLIER =  1.0f;

    private float bodyTiltAmount = 0.0f; // 0 = upright mode, 1 = ground mode
    private Rotation uprightRotation;
    public static final float GROUND_MODE_TILT_RADIANS = (float)Math.PI*0.4f;// / 2;
    public static final float VIEW_MODE_TRANSITION_DURATION = 3.000f; // sec

    public final boolean CHAOTIC_JUMP = false;
    private float rotationZ = 0;
    private float rotationY = 0;
    private float rotationSpeedZ = 4;
    private float rotationSpeedY = -0.3f;//-2.0f;

    @Override
    public void setup(Jumper jumper) {
        jumper.addObserver(this);
        uprightRotation = jumper.getBodyRotation();
//        tiltBodyIntoGroundMode(jumper);
    }

    private void tiltBodyIntoGroundMode(Jumper jumper) {
//        jumper
//        Rotation bodyRotation = jumper.getBodyRotation();
//        jumper.setBodyRotation(bodyRotation.rotate(new Vector(0, 0, 1), (float)-Math.PI / 2));
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

        if (CHAOTIC_JUMP) {
            // Handle tilting between view modes
            handleBodyTilting(deltaTime, jumper);
        }


        if (parachutePulled){
            return new ParachuteFallingState();
        }

        return null;
    }

    private void handleBodyTilting(float deltaTime, Jumper jumper) {
        if (bodyTiltAmount < 1.0) {
            bodyTiltAmount += deltaTime / VIEW_MODE_TRANSITION_DURATION;
            bodyTiltAmount = FallingMath.clamp01(bodyTiltAmount);
        }

        rotationY += rotationSpeedY * deltaTime;
        rotationZ += rotationSpeedZ * deltaTime;

        rotationSpeedY -= rotationSpeedY * 0.3 * deltaTime;
        rotationSpeedZ -= rotationSpeedZ * 0.65 * deltaTime;

        float tilt_radians = GROUND_MODE_TILT_RADIANS*(float)FallingMath.interpolateSmooth(bodyTiltAmount);
        jumper.setBodyRotation(uprightRotation.rotate(uprightRotation.getRight(), -tilt_radians)
                                .rotate(new Vector(0,0,1), -rotationZ)
                                .rotate(new Vector(0,1,0), -rotationY));
    }

    private Vector calculateAcceleration(Jumper jumper, float deltaTime) {
        return calculateAccelerationY(jumper, deltaTime).add(calculateAccelerationXZ(jumper, deltaTime));
    }

    private Vector calculateAccelerationY(Jumper jumper, float deltaTime) {
        float yVelocitySquared = (float) Math.pow(jumper.getVelocity().getY(), 2);

        float drag = 0.5f * World.AIR_DENSITY * yVelocitySquared * jumper.getArea() * jumper.getDragCoefficient();
        float newY = (World.GRAVITATION * 90 + drag) / 90;

        return new Vector(0, newY, 0).scale(Y_ACCELERATION_MULTIPLIER);
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
        return newAcceleration.scale(XZ_ACCELERATION_MULTIPLIER);
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
