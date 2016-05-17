package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;

public class FallingGame {
    private Jump currentJump;

    public FallingGame() {
        currentJump = new Jump();
    }

    public void update(float deltaTime) {
        currentJump.update(deltaTime);
    }

    public void screenClicked(boolean screenClicked) {
        currentJump.getJumper().setScreenClicked(screenClicked);
        //Can be done before you've stopped
        if(currentJump.getJumper().getFallState() instanceof LandedState){
            this.resetGame();
        }
    }

    public Jump getCurrentJump() {
        return currentJump;
    }

    public void setJumperHeadRotation(Rotation rotation) {
        currentJump.getJumper().setHeadRotation(rotation);
    }

//    public void setLookDirection(Vector vector) {
//        currentJump.getJumper().setLookDirection(vector);
//    }

//    public void setUpVector(Vector vector) { currentJump.getJumper().setUpVector(vector);}

    public Vector getLookDirection() {
        return currentJump.getJumper().getLookDirection();
    }
    
    private void setCurrentJump(Jump jump){
        this.currentJump = jump;
    }

    private void resetGame(){
        setCurrentJump(new Jump());
    }

    public Rotation getJumperBodyRotation() {
        return currentJump.getJumper().getBodyRotation();
    }
}