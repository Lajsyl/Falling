package dat367.falling.core;

import dat367.falling.math.Vector;

public class FallingGame {
    private Jump currentJump;

    public FallingGame() {
        currentJump = new Jump();
    }

    public void update(float deltaTime) {
        currentJump.update(deltaTime);
    }

    public Jump getCurrentJump() {
        return currentJump;
    }

    public void setLookDirection(Vector vector) {
        currentJump.getJumper().setLookDirection(vector);
    }

    public Vector getLookDirection() {
        return currentJump.getJumper().getLookDirection();
    }
}
