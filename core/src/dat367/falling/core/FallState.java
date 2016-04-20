package dat367.falling.core;

public interface FallState {
    void setup(Jumper jumper);
    FallState handleFalling(float deltaTime, Jumper jumper);
}
