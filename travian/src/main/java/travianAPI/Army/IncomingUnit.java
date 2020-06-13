package travianAPI.Army;

public class IncomingUnit {
    private int number, timeDuration;
    private MovementType type;

    public IncomingUnit(MovementType type, int number, int timeDuration) {
        this.type = type;
        this.number = number;
        this.timeDuration = timeDuration;
    }
}
