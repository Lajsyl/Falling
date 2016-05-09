package dat367.falling.core;

import dat367.falling.math.Vector;

public abstract class Collidable {

    private Positioned positioned;
    private String name;

    public Collidable(Positioned positioned, String name){
        this.positioned = positioned;
        this.name = name;
    }


    public Vector getPosition(){
        return positioned.getPosition();
    }

    public abstract boolean collidesWith(Collidable collidable);

    public static boolean areColliding(Collidable c1, Collidable c2){
        if(c1 instanceof SphereCollider && c2 instanceof SphereCollider){
            float r1 = ((SphereCollider) c1).getRadius();
            float r2 = ((SphereCollider) c2).getRadius();
            float distance = c1.getPosition().sub(c2.getPosition()).lengthSquared();
            return distance < (r1 + r2) * (r1 + r2);
        }

        return false;
    }

}
