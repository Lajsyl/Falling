package dat367.falling.core;

public class SphereCollider extends Collider {

    private float radius;

    public SphereCollider(Positioned positioned, String name, float radius){
        super(positioned, name);
        this.radius = radius;
    }

    @Override
    public boolean collidesWith(Collider collider) {

        return Collider.areColliding(this, collider);
    }

    public float getRadius(){
        return radius;
    }
}
