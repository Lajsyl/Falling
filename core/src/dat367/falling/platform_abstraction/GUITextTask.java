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
}
