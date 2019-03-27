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
}