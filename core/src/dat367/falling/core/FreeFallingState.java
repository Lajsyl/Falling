package dat367.falling.core;

import dat367.falling.core.world.World;
import dat367.falling.math.Vector;

import java.util.Observable;
import java.util.Observer;

public class FreeFallingState implements FallState, Observer {

    private boolean parachutePulled = false;


    public FreeFallingState(Jumper jumper){
        observe(jumper);
    }

    @Override
    public void setup(Jumper jumper) {

    }

    @Override
    public FallState handleFalling(float deltaTime, Jumper jumper) {

        jumper.setAcceleration(calculateAcceleration(jumper));
        Vector v0 = new Vector(jumper.getVelocity());
        jumper.setVelocity(calculateVelocity(deltaTime, jumper));
        jumper.setPosition(calculatePosition(deltaTime, jumper, v0));


        if (parachutePulled){
            return new ParachuteFallingState();
        }

        return null;
    }

    private Vector calculateAcceleration(Jumper jumper){

        return calcAccY(jumper).add(calcAccXZ(jumper));
    }


    //Denna gör att acc i Y-led går från 9.82 mot 0
    //Hamnar på ca -51, verklighet ca -56
    private Vector calcAccY(Jumper jumper){
        float drag = (float)(0.5*0.8*1.2041*0.70)*jumper.getVelocity().getY()*jumper.getVelocity().getY();
        float newY =(World.GRAVITATION*90 + drag)/90;
        return new Vector(0, newY, 0);
    }


    //Skalning är väldigt tveksam just nu
    private Vector calcAccXZ(Jumper jumper){
        Vector v = jumper.getLookDirection().normalized();
        v = v.projectedXZ();
        return v.scale(-15f);
    }

    
    private Vector calculateVelocity(float deltaTime, Jumper jumper){
        return new Vector(calcV(deltaTime, jumper.getVelocity().getX(), jumper.getAcceleration().getX()),
                calcV(deltaTime, jumper.getVelocity().getY(), jumper.getAcceleration().getY()),
                calcV(deltaTime, jumper.getVelocity().getZ(), jumper.getAcceleration().getZ()));
    }

    private float calcV(float deltaTime, float v0, float a){
       return v0 + a*deltaTime;
    }

    private Vector calculatePosition(float deltaTime, Jumper jumper, Vector v0){

        return new Vector(calcPos(deltaTime, jumper.getPosition().getX(), jumper.getVelocity().getX(), v0.getX()),
                calcPos(deltaTime, jumper.getPosition().getY(), jumper.getVelocity().getY(), v0.getY()),
                calcPos(deltaTime, jumper.getPosition().getZ(), jumper.getVelocity().getZ(), v0.getZ()));
    }

    //calculates one coordinate of a new position vector
    private float calcPos(float deltaTime, float pos, float v, float v0){
        return pos +(v + v0)*deltaTime/2;
    }


    public void observe(Observable o){
        o.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        parachutePulled = ((Jumper) o).getScreenClicked();
    }
}
