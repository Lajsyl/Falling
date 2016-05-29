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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SphereCollider that = (SphereCollider) o;

        return Float.compare(that.radius, radius) == 0;

    }

    @Override
    public int hashCode() {
        return (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
    }
}
