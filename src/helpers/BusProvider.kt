package helpers

import models.Bus
import models.Stigma

import java.io.File
import java.io.FileNotFoundException
import java.util.ArrayList
import java.util.Scanner

object BusProvider {
    @Throws(FileNotFoundException::class)
    fun fetchBuses(): List<Bus> {
        val input = Scanner(File("/Users/jimfilippou/Projects/distributed/src/Data/busLines.txt"))
        // Scanner input = new Scanner(new File("/home/jimfilippou/IdeaProjects/distributed/src/Data/busLines.txt"));
        input.useDelimiter("-\n")
        val buses = ArrayList<models.Bus>()
        while (input.hasNextLine()) {
            val data = input.nextLine()
            val lineNumber = data.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val busLineID = data.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val lineName = data.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
            val bus = Bus(lineNumber, busLineID, lineName)
            buses.add(bus)
        }
        return buses
    }

    @Throws(FileNotFoundException::class)
    fun readBusPositions(busID: Int): ArrayList<Stigma> {
        val input = Scanner(File("/Users/jimfilippou/Projects/distributed/src/Data/busPositions.txt"))
        input.useDelimiter("-\n")
        val response = ArrayList<Stigma>()
        while (input.hasNextLine()) {
            val data = input.nextLine().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val busId = data[0]
            val x = java.lang.Double.valueOf(data[3])
            val y = java.lang.Double.valueOf(data[4])
            val timestamp = data[5]
            if (busID.toString() == busId) {
                response.add(Stigma(x, y, timestamp))
            }
        }
        return response
    }
}
