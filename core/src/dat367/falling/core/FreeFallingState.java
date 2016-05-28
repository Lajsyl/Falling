package dat367.falling.core;

import dat367.falling.math.FallingMath;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;

public class FreeFallingState implements FallState {

    private boolean parachutePulled = false;

    public static final float XZ_ACCELERATION_MULTIPLIER =  1.0f;
    public static final float Y_ACCELERATION_MULTIPLIER =  1.0f;

    private float bodyTiltAmount = 0.0f; // 0 = upright mode, 1 = ground
    private Rotation uprightRotation;
    public static final float GROUND_MODE_TILT_RADIANS = (float)Math.PI*0.4f;// / 2;
    public static final float VIEW_MODE_TRANSITION_DURATION = 3.000f; // sec

    public final boolean CHAOTIC_JUMP = false;
    private float rotationZ = 0;
    private float rotationY = 0;
    private float rotationSpeedZ = 4;
    private float rotationSpeedY = -0.3f;//-2.0f;

    private PositionedSound fallingWind; // Has max volume when tilting straight towards ground
    private PositionedSound tiltingWind; // Has max volume when tilting fully towards a side, is positioned on that side

    //TODO: Denna ska inte ha "sig själv"
    private FallState impendingState = null;

    @Override
    public void setup(final Jumper jumper) {
        uprightRotation = jumper.getBodyRotation();
        fallingWind = new PinnedPositionedSound(jumper.fallingWindSound, jumper, new Vector(0, -3, 0));
        fallingWind.loop();
        tiltingWind = new PinnedPositionedSound(jumper.tiltingWindSound, jumper, new Vector(0, -3, 0), 0.0f);
        tiltingWind.loop();
        NotificationManager.getDefault().addObserver(CollisionManager.ISLAND_COLLISION_EVENT_ID, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                float x = jumper.getPosition().getX();
                float z = jumper.getPosition().getZ();
                float y = ((HeightMapCollider)event.data.getOtherObject()).getHeight(x, z) + Jumper.BODY_HEIGHT;
                jumper.setPosition(x, y, z);
                impendingState = new CrashedState(false);
            }
        });
        NotificationManager.getDefault().addObserver(FallingGame.SCREEN_TAP_EVENT, new NotificationManager.EventHandler() {
            @Override
            public void handleEvent(NotificationManager.Event event) {
                parachutePulled = true;
            }
        });

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
            fallingWind.stop();
            tiltingWind.stop();
            PositionedSound parachuteOpeningPositionedSound = new PinnedPositionedSound(jumper.parachuteOpeningSound, jumper, new Vector(0, 1, 0));
            parachuteOpeningPositionedSound.play();
            return new ParachuteFallingState();
        }
        if (impendingState != null) {
            return impendingState;
        }
        if (jumper.getPosition().getY() <= Jumper.BODY_HEIGHT){
            PositionedSound landingWaterPositionedSound = new PositionedSound(jumper.landingWaterSound, jumper.getPosition().add(new Vector(0,-1,0)));
            landingWaterPositionedSound.play();
            return new CrashedState(false);
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
        float drag;
        if (jumper.getVelocity().getY() < 0) {
            drag = 0.5f * World.AIR_DENSITY * yVelocitySquared * jumper.getArea() * jumper.getDragCoefficient();
        } else {
            drag = 0;
        }
        float newY = (World.GRAVITATION * 90 + drag) / 90;

        return new Vector(0, newY, 0).scale(Y_ACCELERATION_MULTIPLIER);
    }

    private Vector calculateAccelerationXZ(Jumper jumper, float deltaTime) {
        // Calculate target velocity
        Vector targetVelocityNonScaled = jumper.getLookDirection().normalized().projectOntoPlaneXZ();
        Vector targetVelocity = targetVelocityNonScaled.scale(100);

        // Clamp speed
        float maxSpeed = 50f;
        if (targetVelocity.length() > maxSpeed) {
            targetVelocity = targetVelocity.normalized().scale(maxSpeed);
        }


        //TODO find a good-looking way to do this
        float turnAmount = targetVelocity.length()/maxSpeed;
        jumper.setArea(Jumper.BODY_AREA - (Jumper.BODY_AREA-Jumper.BODY_AREA_AT_FULL_TURN) * turnAmount);

        // The more you turn, the more distorted the wind sound is
        tiltingWind.setVolume(turnAmount * 0.5f);
        fallingWind.setVolume((1.0f - turnAmount * 0.8f) * 0.9f);

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

    private float calculateArea(){
        float turnAmount = 0;
        return Jumper.PARACHUTE_AREA - (Jumper.PARACHUTE_AREA-Jumper.PARACHUTE_AREA_AT_FULL_TURN) * turnAmount;

    }

    @Override
    public String toString() {
        return "Free falling";
    }
}
