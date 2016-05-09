package dat367.falling.core;

public abstract class Collidable {

    private Positioned positioned;

    public Collidable(Positioned positioned){
        this.positioned = positioned;
    }


    public abstract boolean collidesWith(Collidable collidable);

}
