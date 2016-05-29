package dat367.falling.core;

import dat367.falling.math.Rotation;

public class FallingGame {

    private Jump currentJump;

    public static final String BEFORE_GAME_RESTART_EVENT = "BeforeGameRestartEvent";
    public static final String AFTER_GAME_RESTART_EVENT = "AfterGameRestartEvent";
    public static final String SCREEN_TAP_EVENT = "ScreenTapEvent";
    public static final String SCREEN_DOUBLE_TAP_EVENT = "ScreenDoubleTapEvent";

    public FallingGame() {
        currentJump = new Jump();
        setupScreenTapEventHandlers();
    }

    private void setupScreenTapEventHandlers() {
        NotificationManager.getDefault().addObserver(SCREEN_TAP_EVENT, new NotificationManager.EventHandler() {
            @Override
            public void handleEvent(NotificationManager.Event event) {
                //Cannot be done before you've stopped
                if (currentJump.getJumper().getFallState() instanceof LandedState || currentJump.getJumper().getFallState() instanceof CrashedState) {
                    restartGame();
                }
            }
        });

        NotificationManager.getDefault().addObserver(SCREEN_DOUBLE_TAP_EVENT, new NotificationManager.EventHandler() {
            @Override
            public void handleEvent(NotificationManager.Event event) {
                restartGame();
            }
        });
    }

    public void update(float deltaTime) {
        currentJump.update(deltaTime);
    }

    public Jump getCurrentJump() {
        return currentJump;
    }

    public void setJumperHeadRotation(Rotation rotation) {
        currentJump.getJumper().setHeadRotation(rotation);
    }

    private void setCurrentJump(Jump jump) {
        this.currentJump = jump;
    }

    private void restartGame() {
        NotificationManager.getDefault().registerEvent(BEFORE_GAME_RESTART_EVENT, this);
        setCurrentJump(new Jump());
        setupScreenTapEventHandlers();
        NotificationManager.getDefault().registerEvent(AFTER_GAME_RESTART_EVENT, this);
    }

    public Rotation getJumperBodyRotation() {
        return currentJump.getJumper().getBodyRotation();
    }
}
