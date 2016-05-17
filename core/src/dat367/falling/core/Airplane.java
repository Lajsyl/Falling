package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class Airplane {

    public static final Rotation LOOK_OUT_DIRECTION = new Rotation(new Vector(0.975f, 0, -0.070f).normalized(), new Vector(0, 1, 0));
    public static final Vector HEAD_START_POSITION_OFFSET = new Vector(0.2f, 0.5f, -1.95f);

    private Vector position;

    private Model airplaneModel;
    private RenderTask airplaneRenderTask;

    public Airplane(ResourceRequirements resourceRequirements, Vector position) {
        this.position = position;

        airplaneModel = new Model("airplane.g3db");
        resourceRequirements.require(airplaneModel);

        airplaneRenderTask = new ModelRenderTask(airplaneModel, position, new Vector(0, 0, 0), new Vector(1, 1, 1));
    }

    public void update(float deltaTime) {
        RenderQueue.addTask(airplaneRenderTask);
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
