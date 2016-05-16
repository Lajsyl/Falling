package dat367.falling.platform_abstraction;

import dat367.falling.math.Vector;

public class GUITask {

    protected Vector position;

    public GUITask(Vector position){
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }
}
