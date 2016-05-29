package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class DefaultWorld implements World {

    private Ground ground;
    private Island island;
    private CloudSimulator cloudSimulator;
    private Jumper jumper;
    private Airplane airplane;


    public DefaultWorld(ResourceRequirements resourceRequirements) {
        CollisionManager.clear();

        airplane = new Airplane(resourceRequirements, new Vector(0, 3700, 0));
        ground = new Ground(resourceRequirements);
        island = new Island(resourceRequirements, new Vector(0,0,0));

        // Create jumper using the airplane metrics
        jumper = new Jumper(resourceRequirements, airplane.getHeadStartPosition(), airplane.getLookOutDirection());
        cloudSimulator = new CloudSimulator(resourceRequirements, jumper);
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);
        ground.update(deltaTime);
        island.update(deltaTime);
        cloudSimulator.update(deltaTime, jumper, airplane);
        airplane.update(deltaTime);
        CollisionManager.update(deltaTime);
    }

    public Jumper getJumper() {
        return jumper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultWorld that = (DefaultWorld) o;

        if (ground != null ? !ground.equals(that.ground) : that.ground != null) return false;
        if (island != null ? !island.equals(that.island) : that.island != null) return false;
        if (cloudSimulator != null ? !cloudSimulator.equals(that.cloudSimulator) : that.cloudSimulator != null)
            return false;
        if (jumper != null ? !jumper.equals(that.jumper) : that.jumper != null) return false;
        return airplane != null ? airplane.equals(that.airplane) : that.airplane == null;

    }

    @Override
    public int hashCode() {
        int result = ground != null ? ground.hashCode() : 0;
        result = 31 * result + (island != null ? island.hashCode() : 0);
        result = 31 * result + (cloudSimulator != null ? cloudSimulator.hashCode() : 0);
        result = 31 * result + (jumper != null ? jumper.hashCode() : 0);
        result = 31 * result + (airplane != null ? airplane.hashCode() : 0);
        return result;
    }
}
