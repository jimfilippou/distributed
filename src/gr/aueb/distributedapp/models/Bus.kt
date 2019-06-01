package gr.aueb.distributedapp.models

class Bus(private val lineNumber: String, private val busLineID: String, private val lineName: String) {
//    private val routeCode: String
//    private val vehicleId: String
//    private val info: String

    init {
//        this.routeCode = routeCode
//        this.vehicleId = vehicleId
//        this.info = info
    }

    override fun toString(): String {
        return "Line number: " + this.lineNumber + "\nBus line ID: " + this.busLineID + "\nLine name: " + this.lineName
    }
}