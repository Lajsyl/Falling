package dat367.falling.core;


import dat367.falling.math.FallingMath;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;


public class ParachuteFallingState implements FallState {

    private float rotationalSpeed;
    private float rotationalAcceleration;

    public static final float MAX_ROTATIONAL_SPEED = 0.03f;

    public static final boolean USE_ABSOLUTE_STEERING = false;

    private float forwardSpeed = 30;

    private HeightMapCollider landingData;
    private boolean landing = false;

    private PositionedSound parachuteWind;

    @Override
    public void setup(Jumper jumper) {
        Rotation initialParachuteRotation = decideInitialParachuteRotation(jumper);
        jumper.setAdjustmentRotation(initialParachuteRotation.relativeTo(jumper.getBodyRotation()));
        jumper.setBodyRotation(initialParachuteRotation);
        jumper.setVelocity(jumper.getVelocity().add(jumper.getBodyRotation().getDirection().scale(10)));
        jumper.setDragCoefficient(jumper.PARACHUTE_DRAG_COEFFICIENT);
        jumper.setArea(jumper.PARACHUTE_AREA);
        parachuteWind = new PinnedPositionedSound(jumper.parachuteWindSound, jumper, new Vector(0, 2.5f, 0));
        parachuteWind.loop();
        NotificationManager.getDefault().addObserver(CollisionManager.ISLAND_COLLISION_EVENT_ID, new NotificationManager.EventHandler<CollisionManager.CollisionData>() {
            @Override
            public void handleEvent(NotificationManager.Event<CollisionManager.CollisionData> event) {
                landingData = (HeightMapCollider)event.data.getOtherObject();
                landing = true;
            }
        });
    }

    private Rotation decideInitialParachuteRotation(Jumper jumper) {
        Vector uprightHeadDirection;
        // Decide on a sensible direction for the parachute to be faced towards initially
        if (jumper.getHeadRotation().getDirection().getY() >= 0) {
            if (jumper.getHeadRotation().getUp().getY() <= 0) {
                uprightHeadDirection = jumper.getHeadRotation().getUp().scale(-1).projectOntoPlaneXZ().normalized();
            } else {
                uprightHeadDirection = jumper.getHeadRotation().getDirection().projectOntoPlaneXZ().normalized();
            }
        } else {
            uprightHeadDirection = jumper.getHeadRotation().getUp().projectOntoPlaneXZ().normalized();
        }
        Rotation uprightHeadRotation = new Rotation(uprightHeadDirection, new Vector(0, 1, 0));
        return uprightHeadRotation;
    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        if(landing){
            return new LandedState((landingData));
        }
        jumper.setAcceleration(calculateAcceleration(jumper));

        Vector initialVelocity = jumper.getVelocity();

        rotationalAcceleration = calculateRotationalAcceleration(deltaTime, jumper);
        rotationalSpeed = calculateRotationalSpeed(deltaTime);
        jumper.setBodyRotation(new Rotation(jumper.getBodyRotation().getDirection().rotateAroundY(rotationalSpeed), jumper.getBodyRotation().getUp()));

        Vector velocity = calculateVelocity(deltaTime, jumper);

        jumper.setVelocity(velocity);
        jumper.setPosition(calculatePosition(deltaTime, jumper, initialVelocity));

        if (jumper.getPosition().getY() <= Jumper.BODY_HEIGHT){
            parachuteWind.stop();
            PositionedSound landingWaterPositionedSound = new PositionedSound(jumper.landingWaterSound, jumper.getPosition().add(new Vector(0,-1,0)));
            landingWaterPositionedSound.play();
            return new CrashedState(true);
        }
        return null;
    }

    private Vector calcAccXZ(Jumper jumper) {
        return new Vector(0, 0, 0);
    }

    private Vector calculateAcceleration(Jumper jumper){
        return calcAccY(jumper).add(calcAccXZ(jumper));
    }

    private Vector calcAccY(Jumper jumper){
        float yVelocitySquared = (float) Math.pow(jumper.getVelocity().getY(), 2);

        float drag;
        if (jumper.getVelocity().getY() < 0) {
            drag = 0.5f * World.AIR_DENSITY * yVelocitySquared * jumper.getArea() * jumper.getDragCoefficient();
        } else {
            drag = 0;
        }

        float newY = (World.GRAVITATION*90 + drag)/90;

        return new Vector(0,newY,0);
    }

    private Vector calculateVelocity(float deltaTime, Jumper jumper){
        return new Vector(0, jumper.getVelocity().getY(), 0).add(jumper.getAcceleration().scale(deltaTime))
                .add(jumper.getBodyRotation().getDirection().scale(forwardSpeed));
    }

    private float calculateRotationalAcceleration(float deltaTime, Jumper jumper) {
        Vector up = new Vector(0, 1, 0);

        Vector rightDirection;
        if (USE_ABSOLUTE_STEERING) {
            // ABSOLUTE STEERING
            rightDirection = jumper.getBodyRotation().getDirection().projectOntoPlaneXZ().normalized().cross(up);
        } else {
            // RELATIVE-TO-HEAD STEERING
            rightDirection = jumper.getHeadRotation().getRight().projectOntoPlaneXZ().normalized();
        }

        Vector projected = jumper.getHeadRotation().getUp().projectOntoLine(rightDirection);

        Float rollAmount = rightDirection.sub(projected).length() - 1;

        jumper.setArea(calculateArea(Math.abs(rollAmount)));

        float maxRollAmount = 0.5f;
        rollAmount = FallingMath.clamp(rollAmount, -maxRollAmount, maxRollAmount);
        rollAmount = rollAmount / maxRollAmount;

        float targetRotationalSpeed = rollAmount * MAX_ROTATIONAL_SPEED;

        return (targetRotationalSpeed - rotationalSpeed) * 2f;
    }

    private float calculateRotationalSpeed(float deltaTime){
        float speed = rotationalSpeed+(rotationalAcceleration*deltaTime);
        if(speed<0.0000003f && speed>-0.000003f){
            return 0;
        }
        return speed;
    }

    private Vector calculatePosition(float deltaTime, Jumper jumper, Vector v0){
        return jumper.getPosition().add(jumper.getVelocity().add(v0).scale(deltaTime/2));
    }

    private float calculateArea(float turnAmount){
        return Jumper.PARACHUTE_AREA - (Jumper.PARACHUTE_AREA-Jumper.PARACHUTE_AREA_AT_FULL_TURN) * turnAmount;

    }

    @Override
    public String toString() {
        return "Parachute";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParachuteFallingState that = (ParachuteFallingState) o;

        if (Float.compare(that.rotationalSpeed, rotationalSpeed) != 0) return false;
        if (Float.compare(that.rotationalAcceleration, rotationalAcceleration) != 0) return false;
        if (Float.compare(that.forwardSpeed, forwardSpeed) != 0) return false;
        if (landing != that.landing) return false;
        if (landingData != null ? !landingData.equals(that.landingData) : that.landingData != null) return false;
        return parachuteWind != null ? parachuteWind.equals(that.parachuteWind) : that.parachuteWind == null;

    }

    @Override
    public int hashCode() {
        int result = (rotationalSpeed != +0.0f ? Float.floatToIntBits(rotationalSpeed) : 0);
        result = 31 * result + (rotationalAcceleration != +0.0f ? Float.floatToIntBits(rotationalAcceleration) : 0);
        result = 31 * result + (forwardSpeed != +0.0f ? Float.floatToIntBits(forwardSpeed) : 0);
        result = 31 * result + (landingData != null ? landingData.hashCode() : 0);
        result = 31 * result + (landing ? 1 : 0);
        result = 31 * result + (parachuteWind != null ? parachuteWind.hashCode() : 0);
        return result;
    }
}
