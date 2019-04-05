package Models;

public class Bus {

    private String lineNumber;
    private String lineName;
    private String busLineID;

    private String _routeCode;
    private String _vehicleId;
    private String _info;

    public Bus(String lineNumber, String busLineID, String lineName) {
        this.lineNumber = lineNumber;
        this.busLineID = busLineID;
        this.lineName = lineName;
    }

    @Override
    public String toString() {
        return "Line number: " + this.lineNumber + "\nBus line ID: " + this.busLineID + "\nLine name: " + this.lineName;
    }
}