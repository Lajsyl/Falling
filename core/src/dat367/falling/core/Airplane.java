package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Model;
import dat367.falling.platform_abstraction.ModelRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Airplane {

    public static final Vector VELOCITY = new Vector(0, 0, 85f);

    public static final Rotation LOOK_OUT_DIRECTION = new Rotation(new Vector(0.975f, 0, -0.070f).normalized(), new Vector(0, 1, 0));
    public static final Vector HEAD_START_POSITION_OFFSET = new Vector(0.0f, 0.5f, -1.95f);

    private Vector position;
    private Vector actualVelocity = new Vector(0, 0, 0);
    private boolean playerHasJumpedOffAirplane = false;
    private final float ACCELERATION_SCALE = 0.3f;

    private Model airplaneModel;

    public Airplane(ResourceRequirements resourceRequirements, Vector position) {
        this.position = position;

        airplaneModel = new Model("airplane.g3db");
        resourceRequirements.require(airplaneModel);

        NotificationManager.addObserver(FallState.STATE_CHANGED_EVENT_ID, new NotificationManager.EventHandler<FallState>() {
            @Override
            public void handleEvent(NotificationManager.Event<FallState> event) {
                // If the fall state changed to FreeFalling state, the player has jumped off the airplane
                if (event.data instanceof FreeFallingState) {
                    playerHasJumpedOffAirplane = true;
                }
            }
        });
    }

    public void update(float deltaTime) {
        if (playerHasJumpedOffAirplane) {
            Vector missingVelocity = VELOCITY.sub(actualVelocity);
            actualVelocity = actualVelocity.add(missingVelocity.scale(deltaTime * ACCELERATION_SCALE));

            position = position.add(actualVelocity.scale(deltaTime));
        }

        RenderQueue.addTask(new ModelRenderTask(
                airplaneModel,
                position,
                new Rotation(),
                new Vector(1, 1, 1)
        ));
    }

    public Vector getActualVelocity() {
        return actualVelocity;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getHeadStartPosition() {
        return position.add(HEAD_START_POSITION_OFFSET);
    }

    public Rotation getLookOutDirection() {
        return LOOK_OUT_DIRECTION;
    }
}
