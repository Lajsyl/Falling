package dat367.falling;

public class FallingGame {
    private Jump currentJump;

    public void update(float deltaTime) {
        currentJump.update(deltaTime);
    }
}
