package travianAPI.Army;

import java.util.ArrayList;

public class UnitMovement {
    private ArrayList <IncomingUnit> incomingUnits;
    private ArrayList <OutgoingUnit> outgoingUnits;
    private ArrayList <UnknownUnit> unknownUnits;

    public UnitMovement(ArrayList <IncomingUnit> incomingUnits, ArrayList <OutgoingUnit> outgoingUnits, ArrayList <UnknownUnit> unknownUnits){
        this.incomingUnits = incomingUnits;
        this.outgoingUnits = outgoingUnits;
        this.unknownUnits = unknownUnits;
    }

    public ArrayList<IncomingUnit> getIncomingUnits() {
        return incomingUnits;
    }

    public ArrayList<OutgoingUnit> getOutgoingUnits() {
        return outgoingUnits;
    }

    public ArrayList<UnknownUnit> getUnknownUnits() {
        return unknownUnits;
    }

}
