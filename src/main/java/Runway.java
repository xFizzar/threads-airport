import lombok.Data;

@Data
public class Runway{

    private final RunwayDeclaration runwayDec;

    private boolean isBlocked;

    public Runway(RunwayDeclaration dec) {
        this.runwayDec = dec;
        this.isBlocked = false;
    }


}
