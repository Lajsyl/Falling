package dat367.falling.core;

import dat367.falling.math.Vector;

public abstract class Collider {

    private Positioned positioned;
    private String name;
    private boolean enabled = true;

    public Collider(Positioned positioned, String name){
        this.positioned = positioned;
        this.name = name;
    }


    public Vector getPosition(){
        return positioned.getPosition();
    }

    public String getName(){
        return name;
    }

    public boolean getEnabled(){
        return enabled;
    }

    void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public abstract boolean collidesWith(Collider collider);

    public static boolean areColliding(Collider c1, Collider c2){
        if (c1.getEnabled() && c2.getEnabled()) {
            if (c1 instanceof SphereCollider && c2 instanceof SphereCollider) {
                float r1 = ((SphereCollider) c1).getRadius();
                float r2 = ((SphereCollider) c2).getRadius();
                float distance = c1.getPosition().sub(c2.getPosition()).lengthSquared();
                return distance < (r1 + r2) * (r1 + r2);
            }
        }
        return false;
    }

}
