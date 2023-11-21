import lombok.Data;

@Data
public class Plane implements Runnable{

    private final int number;

    private Tower tower;

    private PlaneStates planeState;

    private Runway landingRunway;
    private Runway otherRunway;


    public Plane(int number, Runway landingRunway, Runway otherRunway, Tower tower) {
        this.number = number;
        this.landingRunway = landingRunway;
        this.otherRunway = otherRunway;
        this.tower = tower;
    }

    @Override
    public void run() {
        tower.planeStart(this);
    }
}
