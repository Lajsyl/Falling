package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

import java.util.Observable;

public class Jumper extends Observable implements Positioned {

    public static final float MASS = 70.0f;
    public static final float BODY_AREA = 0.8f;
    public static final float BODY_HEIGHT = 1.7f;
    public static final float BODY_AREA_AT_FULL_TURN = 0.9f * BODY_AREA; //0.3f
    public static final float PARACHUTE_AREA = 17.0f;
    public static final float PARACHUTE_AREA_AT_FULL_TURN = 0.3f * PARACHUTE_AREA;
    public static final float DRAG_COEFFICIENT = 1.1f;
    public static final float PARACHUTE_DRAG_COEFFICIENT = 1.4f;

    private FallState fallState = new PreJumpState();


    private Model parachute = new Model("parachute.g3db", false);

    public final Sound airplaneWindSound = new Sound("wind05.wav");
    public final Sound airplaneLeanoutWindSound = new Sound("wind07.wav");
    public final Sound fallingWindSound = new Sound("wind03.wav");
    public final Sound tiltingWindSound = new Sound("wind08.wav");
    public final Sound parachuteOpeningSound = new Sound("parachute_opening.wav");
    public final Sound parachuteWindSound = new Sound("wind03.wav");
    public final Sound landingWaterSound = new Sound("landing_water.wav");
    public final Sound shoreSound = new Sound("shore.wav");

    private float area = BODY_AREA;
    private float dragCoefficient = DRAG_COEFFICIENT;
    private Vector position;
    private Vector velocity = new Vector(0, 0, 0);
    private Vector acceleration = new Vector(0, 0, 0);
    private Rotation bodyRotation;
    private Rotation headRotation = new Rotation(new Vector(1, 0, 0), new Vector(0, 1, 0));
    private Rotation adjustmentRotation = new Rotation();

    private SphereCollider sphereCollider;
    public static final String NAME = "Jumper";
    public static final String POSITION_CHANGED_EVENT_ID = "JumperPositionChangedEvent";

    public Jumper(ResourceRequirements resourceRequirements, Vector position, Rotation bodyRotation) {

        resourceRequirements.require(parachute);
        resourceRequirements.require(airplaneWindSound);
        resourceRequirements.require(airplaneLeanoutWindSound);
        resourceRequirements.require(fallingWindSound);
        resourceRequirements.require(tiltingWindSound);
        resourceRequirements.require(parachuteOpeningSound);
        resourceRequirements.require(parachuteWindSound);
        resourceRequirements.require(landingWaterSound);
        resourceRequirements.require(shoreSound);

        this.position = position;
        this.sphereCollider = new SphereCollider(this, NAME, 0.5f);
        CollisionManager.addCollider(sphereCollider);
        this.bodyRotation = bodyRotation;

        fallState.setup(this);
    }

    public void update(float deltaTime) {
        FallState newState = fallState.handleFalling(deltaTime, this);
        if (newState != null) {
            this.fallState = newState;
            fallState.setup(this);
            NotificationManager.getDefault().registerEvent(FallState.STATE_CHANGED_EVENT_ID, newState);
        }
        if (fallState instanceof ParachuteFallingState){
            parachuteUpdate();
        }

    }

    public Vector getLookDirection() {
        return headRotation.getDirection();
    }

    public void setAcceleration(float x, float y, float z){
        acceleration = new Vector(x, y, z);
    }

    public void setVelocity(float x, float y, float z){
        velocity = new Vector(x, y, z);
    }

    public void setPosition(float x, float y, float z){
        setPosition(new Vector(x, y, z));
    }

    public Vector getPosition() {
        return position;
    }

    @Override
    public String getPositionChangedEventID() {
        return POSITION_CHANGED_EVENT_ID;
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    public void setPosition(Vector position) {
        this.position = position;
        NotificationManager.getDefault().registerEvent(getPositionChangedEventID(), this);
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public float getArea(){ return area; }

    public void setArea(float area){
        if (area < 0 ){
            throw new IllegalArgumentException("Negative area");
        } else {
            this.area = area;
        }
    }

    public float getDragCoefficient(){
        return this.dragCoefficient;
    }

    public void setDragCoefficient(float coefficient){
        if (coefficient < 0){
            throw new IllegalArgumentException("Negative drag coefficent");
        } else {
            this.dragCoefficient = coefficient;
        }
    }

    public FallState getFallState(){ return fallState; }

    public void setFallState(FallState fallState){ this.fallState = fallState; }

    public String getFallStateDebugString() {
        return fallState.toString();
    }

    public void setHeadRotation(Rotation headRotation) {
        this.headRotation = headRotation;
    }

    public Rotation getBodyRotation() {
        return bodyRotation;
    }

    public Rotation getHeadRotation() {
        return headRotation;
    }

    public void setBodyRotation(Rotation bodyRotation) {
        this.bodyRotation = bodyRotation;
    }

    public Rotation getAdjustmentRotation() { return adjustmentRotation; }

    public void setAdjustmentRotation(Rotation rotation){
        this.adjustmentRotation = rotation;
    }

    private void parachuteUpdate(){
        RenderTask parachuteRender = new ModelRenderTask(parachute, this.position.add(new Vector(0,3,0)), bodyRotation, new Vector(1,1,1));
        RenderQueue.getDefault().addTask(parachuteRender);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jumper jumper = (Jumper) o;

        if (Float.compare(jumper.area, area) != 0) return false;
        if (Float.compare(jumper.dragCoefficient, dragCoefficient) != 0) return false;
        if (fallState != null ? !fallState.equals(jumper.fallState) : jumper.fallState != null) return false;
        if (parachute != null ? !parachute.equals(jumper.parachute) : jumper.parachute != null) return false;
        if (position != null ? !position.equals(jumper.position) : jumper.position != null) return false;
        if (velocity != null ? !velocity.equals(jumper.velocity) : jumper.velocity != null) return false;
        if (acceleration != null ? !acceleration.equals(jumper.acceleration) : jumper.acceleration != null)
            return false;
        if (bodyRotation != null ? !bodyRotation.equals(jumper.bodyRotation) : jumper.bodyRotation != null)
            return false;
        if (headRotation != null ? !headRotation.equals(jumper.headRotation) : jumper.headRotation != null)
            return false;
        if (adjustmentRotation != null ? !adjustmentRotation.equals(jumper.adjustmentRotation) : jumper.adjustmentRotation != null)
            return false;
        return sphereCollider != null ? sphereCollider.equals(jumper.sphereCollider) : jumper.sphereCollider == null;

    }

    @Override
    public int hashCode() {
        int result = fallState != null ? fallState.hashCode() : 0;
        result = 31 * result + (parachute != null ? parachute.hashCode() : 0);
        result = 31 * result + (area != +0.0f ? Float.floatToIntBits(area) : 0);
        result = 31 * result + (dragCoefficient != +0.0f ? Float.floatToIntBits(dragCoefficient) : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (velocity != null ? velocity.hashCode() : 0);
        result = 31 * result + (acceleration != null ? acceleration.hashCode() : 0);
        result = 31 * result + (bodyRotation != null ? bodyRotation.hashCode() : 0);
        result = 31 * result + (headRotation != null ? headRotation.hashCode() : 0);
        result = 31 * result + (adjustmentRotation != null ? adjustmentRotation.hashCode() : 0);
        result = 31 * result + (sphereCollider != null ? sphereCollider.hashCode() : 0);
        return result;
    }
}
