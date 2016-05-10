package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.Quad;
import dat367.falling.platform_abstraction.QuadRenderTask;
import dat367.falling.platform_abstraction.RenderQueue;
import dat367.falling.platform_abstraction.ResourceRequirements;

public class Collectible implements Positioned {

    private SphereCollider sphereCollider;
    private Vector position;

    private Quad quad;


    public Collectible(ResourceRequirements resourceRequirements, Vector position){
        quad = new Quad("balloonfilled.jpg", true, true, 1000, 100, 1, 1, true);
        resourceRequirements.require(quad);
        sphereCollider = new SphereCollider(this, "Collectible",10);
        CollisionManager.addCollider(sphereCollider);
        this.position = position;
    }

    public Vector getPosition(){
        return position;
    }

    public void update(float deltaTime){
        RenderQueue.addTask(new QuadRenderTask(quad, getPosition(), new Vector(0,0,0), new Vector(10,10,10)));
    }
}
