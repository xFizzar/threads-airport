import lombok.Data;

@Data
public class Runway{

    private final RunwayDeclaration runwayDec;

    private boolean isBlocked = false;

    public Runway(RunwayDeclaration dec) {
        this.runwayDec = dec;
    }


}
