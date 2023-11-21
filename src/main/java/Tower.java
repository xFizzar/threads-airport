import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;

@NoArgsConstructor
public class Tower {

    private ArrayList<Plane> planes = new ArrayList<>();

    public void setPlanes(ArrayList<Plane> planes) {
        this.planes = planes;
    }

    @SneakyThrows
    public void planeStart(Plane plane) {
        synchronized (this) {
            while (plane.getLandingRunway().isBlocked()) {
                this.wait();
            }
            planeLanding(plane);
        }


        System.out.println("Plane " + plane.getNumber() + " arrives for runway " + plane.getLandingRunway().getRunwayDec());
        Thread.sleep(3000);

        System.out.println("Plane " + plane.getNumber() + " crosses E on runway " + plane.getLandingRunway().getRunwayDec());
        planeCrossedE(plane);


        Thread.sleep(5000);
        System.out.println("Plane " + plane.getNumber() + " leaves runway " + plane.getLandingRunway().getRunwayDec());
        planeLeavesRunway(plane);


    }

    // Ein Flugzeug startet und blockiert damit beide Runways
    // der PlaneState wird auf BEFORE_E gesetzt
    public synchronized void planeLanding(Plane plane) {
        plane.getLandingRunway().setBlocked(true);
        plane.getOtherRunway().setBlocked(true);

        plane.setPlaneState(PlaneStates.BEFORE_E);
    }

    // Das Flugzeug überfliegt den Punkt E
    // das heißt der andere Runway wird freigegeben, wenn sich auf diesem gerade
    // kein Flugzeug befindet
    // der PlaneState wird auf AFTER_E gesetzt
    public synchronized void planeCrossedE(Plane plane) {
        if (runwayEmpty(plane.getOtherRunway())) {
            plane.getOtherRunway().setBlocked(false);
        }

        plane.setPlaneState(PlaneStates.AFTER_E);
        this.notifyAll();
    }

    // Diese Funktion gibt nur den Runway frei, auf welches das Flugzeug war
    // aber das nur, wenn auf dem anderen Runway das Flugzeug bereits noch dem Punkt E ist
    // der PlaneState wird auf FINISHED gesetzt
    public synchronized void planeLeavesRunway(Plane plane) {
        if (runwayCrossedE(plane.getOtherRunway())) {
            plane.getLandingRunway().setBlocked(false);
        }

        plane.setPlaneState(PlaneStates.FINISHED);
        this.notifyAll();
    }

    // gibt True zurück, wenn sich KEIN Flugzeug auf dem übergebenen Runway befindet,
    // gibt False zurück, wenn sich EIN Flugzeug auf dem übergebenen Runway befindet
    public synchronized boolean runwayEmpty(Runway runway) {
        return planes.stream()
                .filter(plane -> plane.getPlaneState() == PlaneStates.AFTER_E || plane.getPlaneState() == PlaneStates.BEFORE_E)
                .noneMatch(plane -> plane.getLandingRunway() == runway);
    }

    // gibt True zurück, wenn KEIN Flugzeug sich auf dem übergebenen Runway vor dem Punkt E befindet,
    // gibt False zurück, wenn EIN flugzeug sich auf dem übergebenen Runway vor dem Punkt E befindet
    public synchronized boolean runwayCrossedE(Runway runway) {
        return planes.stream()
                .filter(plane -> plane.getPlaneState() == PlaneStates.BEFORE_E)
                .noneMatch(plane -> plane.getLandingRunway() == runway);
    }


}
