package dat367.falling.core;


import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    static List<Collidable> collidables = new ArrayList<Collidable>();
    public static Collidable jumper;

    public static void addCollider(Collidable collidable){
        if(collidable.getName().equals(Jumper.NAME)){
            assert jumper == null;
            jumper = collidable;
        }else
        collidables.add(collidable);
    }

    public static void clear(){
        collidables.clear();
        jumper = null;
    }


    public static void update(float deltaTime){
        for(Collidable c : collidables){
            if(jumper.collidesWith(c)){
                System.out.println("Collided! :D");
            }
        }
    }
}
