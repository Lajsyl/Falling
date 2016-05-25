package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.ResourceRequirements;

import java.util.ArrayList;
import java.util.List;

public abstract class BalloonLevel {

    List<Interactable> interactableList = new ArrayList<Interactable>();
    ResourceRequirements resourceRequirements;

    public BalloonLevel(ResourceRequirements resourceRequirements){
        this.resourceRequirements = resourceRequirements;
    }

    public abstract void create();

    public void update(float deltaTime, Jumper jumper){
        for (Interactable i : interactableList) {
            if (shouldBeRendered(i, jumper)) {
                i.update(deltaTime);
            }
        }
    }

    private boolean shouldBeRendered(Positioned positioned, Jumper jumper) {
        Vector between = positioned.getPosition().sub(jumper.getPosition());
        return between.length() <= BalloonGameMode.BALLOON_MAX_DRAW_DISTANCE;
    }

    public List<Interactable> getInteractableList(){
        return interactableList;
    }

    public ResourceRequirements getResourceRequirements(){
        return resourceRequirements;
    }
}
