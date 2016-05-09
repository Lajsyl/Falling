package dat367.falling.core;

public class SphereCollider extends Collidable {

    private float radius;

    public SphereCollider(Positioned positioned, String name, float radius){
        super(positioned, name);
        this.radius = radius;
    }

    @Override
    public boolean collidesWith(Collidable collidable) {

        return Collidable.areColliding(this, collidable);
    }

    public float getRadius(){
        return radius;
    }
}
