package Models;

public class Bus {

    private String _lineNumber;
    private String _routeCode;
    private String _vehicleId;
    private String _lineName;
    private String _busLineID;
    private String _info;

    public Bus(String lineNumber, String busLineID, String lineName) {
        this._lineNumber = lineNumber;
        this._busLineID = busLineID;
        this._lineName = lineName;
    }

    @Override
    public String toString() {
        return "Line number: " + this._lineNumber + "\nBus line ID: " + this._busLineID + "\nLine name: " + this._lineName;
    }
}