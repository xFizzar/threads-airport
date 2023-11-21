import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        Runway runwayAB = new Runway(RunwayDeclaration.AB);
        Runway runwayCD = new Runway(RunwayDeclaration.CD);


        Tower tower = new Tower();

        ArrayList<Plane> planes = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            if (Math.random() > 0.5){
                planes.add(new Plane(i, runwayAB, runwayCD, tower));
            } else {
                planes.add(new Plane(i, runwayCD, runwayAB, tower));
            }
        }

        tower.setPlanes(planes);

        List<Thread> planesThreads = planes.stream().map(Thread::new).toList();


        // Starting the threads
        for (Thread planesThread : planesThreads) {
            planesThread.start();
        }
    }
}