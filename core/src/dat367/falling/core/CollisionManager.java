package dat367.falling.core;


import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    static List<Collidable> collidables = new ArrayList<Collidable>();

    public static void addCollider(Collidable collidable){
        collidables.add(collidable);
    }

    public static void clear(){
        collidables.clear();
    }


    /*public static void update(float deltaTime){
        for()
    }*/
}
