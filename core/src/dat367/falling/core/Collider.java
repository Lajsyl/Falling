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

    public void setParentEnabled(boolean parentEnabled) {
        positioned.setEnabled(parentEnabled);
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

    void setEnabled(boolean b){
        this.enabled = b;
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
            if (c1 instanceof SphereCollider && c2 instanceof HeightMapCollider) {
                return sphereHeightMapCollision((SphereCollider) c1, (HeightMapCollider) c2);
            }
            if (c1 instanceof HeightMapCollider && c2 instanceof SphereCollider) {
                return sphereHeightMapCollision((SphereCollider) c2, (HeightMapCollider) c1);
            }
        }
        return false;
    }

    private static boolean sphereHeightMapCollision(SphereCollider sphere, HeightMapCollider heightMap) {

        return false;
    }

}
