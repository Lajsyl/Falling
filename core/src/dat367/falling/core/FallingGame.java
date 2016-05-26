package dat367.falling.core;

import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;

public class FallingGame {
    private Jump currentJump;

    public static final String BEFORE_GAME_RESTART_EVENT = "BeforeGameRestartEvent";
    public static final String AFTER_GAME_RESTART_EVENT = "AfterGameRestartEvent";

    public FallingGame() {
        currentJump = new Jump();
    }

    public void update(float deltaTime) {
        currentJump.update(deltaTime);
    }

    public void screenClicked(boolean screenClicked) {
        currentJump.getJumper().setScreenClicked(screenClicked);
        //Can be done before you've stopped
        if(currentJump.getJumper().getFallState() instanceof LandedState || currentJump.getJumper().getFallState() instanceof CrashedState){
            this.restartGame();
        }
    }

    public void deviceShaken(boolean deviceShaken) {

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

    private void restartGame(){
        NotificationManager.getDefault().registerEvent(BEFORE_GAME_RESTART_EVENT, this);
        setCurrentJump(new Jump());
        NotificationManager.getDefault().registerEvent(AFTER_GAME_RESTART_EVENT, this);
    }

    public Rotation getJumperBodyRotation() {
        return currentJump.getJumper().getBodyRotation();
    }

    public void screenDoubleClick() {
        restartGame();
    }
}
