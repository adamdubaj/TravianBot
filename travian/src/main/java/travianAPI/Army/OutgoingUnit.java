package travianAPI.Army;

public class OutgoingUnit {
    private int number, timeDuration;
    private MovementType type;

    public OutgoingUnit(MovementType type, int number, int timeDuration){
        this.type = type;
        this.number = number;
        this.timeDuration = timeDuration;
    }
}
