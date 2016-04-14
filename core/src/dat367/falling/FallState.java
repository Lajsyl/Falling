package dat367.falling;

public interface FallState {
    FallState handleFalling(float deltaTime, Jumper jumper);
    // Get head rotation, set look vector

    // Fall down, acc. move, etc.

}
