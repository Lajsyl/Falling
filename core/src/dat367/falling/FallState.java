package dat367.falling;

public interface FallState {
    void setup(Jumper jumper);
    FallState handleFalling(float deltaTime, Jumper jumper);
}
