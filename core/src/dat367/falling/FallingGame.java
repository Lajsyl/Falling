package dat367.falling;

public class FallingGame {
    private Jump currentJump;
    private ResourceRequirements resourceRequirements;

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
