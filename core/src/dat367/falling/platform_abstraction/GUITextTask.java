package dat367.falling.platform_abstraction;

import dat367.falling.math.Vector;

public class GUITextTask extends GUITask {

    private final String text;
    private final Vector colour;
    private final boolean centerHorizontal;
    private final boolean bigSize;

    public GUITextTask(String text, Vector colour, Vector position, boolean centerHorizontal, boolean bigSize){
        super(position);
        this.text = text;
        this.colour = colour;
        this.centerHorizontal = centerHorizontal;
        this.bigSize = bigSize;
    }

    public String getText() {
        return text;
    }

    public Vector getColour() {
        return colour;
    }

    public boolean shouldCenterHorizontal() {
        return centerHorizontal;
    }

    public boolean isBigSize() {
        return bigSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GUITextTask that = (GUITextTask) o;

        if (centerHorizontal != that.centerHorizontal) return false;
        if (bigSize != that.bigSize) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return colour != null ? colour.equals(that.colour) : that.colour == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        result = 31 * result + (centerHorizontal ? 1 : 0);
        result = 31 * result + (bigSize ? 1 : 0);
        return result;
    }
}
