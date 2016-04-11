package dat367.falling;

public interface FallState {
    FallState handleFalling(float deltaTime, Player player);
    // Get head rotation, set look vector

    // Fall down, acc. move, etc.

}
