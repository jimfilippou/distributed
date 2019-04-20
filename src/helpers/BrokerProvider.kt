package helpers

import models.Broker

import java.io.File
import java.io.FileNotFoundException
import java.util.ArrayList
import java.util.Scanner

object BrokerProvider {
    fun fetchBrokers(): List<Broker> {
        var input: Scanner? = null
        try {
            input = Scanner(File("/Users/jimfilippou/Projects/distributed/src/data/brokers.txt"))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        input!!.useDelimiter("-\n")
        val brokers = ArrayList<Broker>()
        while (input.hasNextLine()) {
            val data = input.nextLine()
            val ip = data.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val port = Integer.parseInt(data.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            val broker = Broker(ip, port)
            brokers.add(broker)
        }
        return brokers
    }
}