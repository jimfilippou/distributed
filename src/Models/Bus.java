package Models;

public class Bus {

    private String lineNumber;
    private String lineName;
    private String busLineID;
    private String routeCode;
    private String vehicleId;
    private String info;

    public Bus(String lineNumber, String busLineID, String lineName) {
        this.lineNumber = lineNumber;
        this.busLineID = busLineID;
        this.lineName = lineName;
        this.routeCode = routeCode;
        this.vehicleId = vehicleId;
        this.info = info;
    }

    @Override
    public String toString() {
        return "Line number: " + this.lineNumber + "\nBus line ID: " + this.busLineID + "\nLine name: " + this.lineName;
    }
}