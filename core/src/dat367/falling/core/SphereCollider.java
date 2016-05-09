package dat367.falling.core;

public class SphereCollider extends Collidable {

    public SphereCollider(Positioned positioned){
        super(positioned);
    }

    @Override
    public boolean collidesWith(Collidable collidable) {
        return false;
    }
}
