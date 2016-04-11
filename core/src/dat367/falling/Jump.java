package dat367.falling;

public class Jump {

    private Player player;
    private World world;

    public void update(float deltaTime) {
        player.update(deltaTime);
        world.update(deltaTime);
    }
}
