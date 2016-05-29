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
    public static final Vector HEAD_START_POSITION_OFFSET = new Vector(-0.06f, 0.6f, -1.95f - 0.15f); // -0.15f so that text is more readible;

    private Vector position;
    private Vector actualVelocity = new Vector(0, 0, 0);
    private boolean playerHasJumpedOffAirplane = false;
    private final float ACCELERATION_SCALE = 0.3f;

    private Model airplaneModel = new Model("airplane.g3db");
    private Model introNoteModel = new Model("intro_note.g3db");
    private Model scoringNoteModel = new Model("scoring_note.g3db");

    public Airplane(ResourceRequirements resourceRequirements, Vector position) {
        this.position = position;

        resourceRequirements.require(airplaneModel);
        resourceRequirements.require(introNoteModel);
        resourceRequirements.require(scoringNoteModel);

        NotificationManager.getDefault().addObserver(FallState.STATE_CHANGED_EVENT_ID, new NotificationManager.EventHandler<FallState>() {
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

        RenderQueue.getDefault().addTask(new ModelRenderTask(
                airplaneModel,
                position,
                new Rotation(),
                new Vector(1, 1, 1)
        ));

        if (!playerHasJumpedOffAirplane) {
            RenderQueue.getDefault().addTask(new ModelRenderTask(
                    introNoteModel,
                    position,
                    new Rotation(),
                    new Vector(1, 1, 1)
            ));
            RenderQueue.getDefault().addTask(new ModelRenderTask(
                    scoringNoteModel,
                    position,
                    new Rotation(),
                    new Vector(1, 1, 1)
            ));
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airplane airplane = (Airplane) o;

        if (playerHasJumpedOffAirplane != airplane.playerHasJumpedOffAirplane) return false;
        if (Float.compare(airplane.ACCELERATION_SCALE, ACCELERATION_SCALE) != 0) return false;
        if (position != null ? !position.equals(airplane.position) : airplane.position != null) return false;
        if (actualVelocity != null ? !actualVelocity.equals(airplane.actualVelocity) : airplane.actualVelocity != null)
            return false;
        if (airplaneModel != null ? !airplaneModel.equals(airplane.airplaneModel) : airplane.airplaneModel != null)
            return false;
        if (introNoteModel != null ? !introNoteModel.equals(airplane.introNoteModel) : airplane.introNoteModel != null)
            return false;
        return scoringNoteModel != null ? scoringNoteModel.equals(airplane.scoringNoteModel) : airplane.scoringNoteModel == null;

    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (actualVelocity != null ? actualVelocity.hashCode() : 0);
        result = 31 * result + (playerHasJumpedOffAirplane ? 1 : 0);
        result = 31 * result + (ACCELERATION_SCALE != +0.0f ? Float.floatToIntBits(ACCELERATION_SCALE) : 0);
        result = 31 * result + (airplaneModel != null ? airplaneModel.hashCode() : 0);
        result = 31 * result + (introNoteModel != null ? introNoteModel.hashCode() : 0);
        result = 31 * result + (scoringNoteModel != null ? scoringNoteModel.hashCode() : 0);
        return result;
    }
}
