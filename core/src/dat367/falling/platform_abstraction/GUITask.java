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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GUITask guiTask = (GUITask) o;

        return position != null ? position.equals(guiTask.position) : guiTask.position == null;

    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }
}
