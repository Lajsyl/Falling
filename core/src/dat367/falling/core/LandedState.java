package dat367.falling.core;

import dat367.falling.math.Vector;

public class LandedState implements FallState {

    private final HeightMapCollider landingTerrain;
    public static final String PLAYER_HAS_STOPPED_EVENT_ID = "PLAYER_HAS_STOPPED_EVENT_ID";

    public LandedState(HeightMapCollider landingTerrain) {
        this.landingTerrain = landingTerrain;
    }

    @Override
    public void setup(Jumper jumper) {
        jumper.setAcceleration(new Vector(0, 0, 0));

        jumper.setVelocity(jumper.getVelocity().projectOntoPlaneXZ());

        Vector position = jumper.getPosition();
        jumper.setPosition(position.getX(), Jumper.BODY_HEIGHT, position.getZ());

        // Create shore sound from multiple directions
        for (float angle = 0; angle < 2*Math.PI; angle += 2*Math.PI/3) {
            Vector soundPos = new Vector(position.getX(), 0, position.getZ()).add(new Vector((float)Math.sin(angle)*5, 0, (float)Math.cos(angle)*5));
            PositionedSound shore = new PositionedSound(jumper.shoreSound, soundPos);
            shore.loop();
        }
    }

    // TODO: Might create a change in position as to mimic the shock when landing
    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {
        jumper.setVelocity(jumper.getVelocity().scale(0.98f));
        jumper.setPosition(jumper.getPosition().add(jumper.getVelocity().scale(deltaTime)));

        float x = jumper.getPosition().getX();
        float z = jumper.getPosition().getZ();
        float groundHeight;

        // "Glue" jumper to the ground
        if (landingTerrain != null) {
            if (landingTerrain.pointIsInsideXZBoundary(jumper.getPosition())) {
                groundHeight = landingTerrain.getHeight(x, z);
            } else {
                groundHeight = landingTerrain.getBasePosition().getY();
            }
            float y = groundHeight + Jumper.BODY_HEIGHT;
            jumper.setPosition(x, y, z);
        }

        if(jumper.getVelocity().lengthSquared() < 0.05f){
            jumper.setVelocity(0, 0 , 0);
            NotificationManager.getDefault().registerEvent(PLAYER_HAS_STOPPED_EVENT_ID, null);
        }

        return null;
    }

    @Override
    public String toString() {
        return "Landed";
    }

}
