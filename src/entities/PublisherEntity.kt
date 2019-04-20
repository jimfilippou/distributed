package entities

import handlers.PublisherHandler
import helpers.BusProvider
import models.Broker
import models.Publisher
import models.Stigma
import models.Wrapper

import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket
import java.util.concurrent.TimeUnit

class PublisherEntity(private val publisher: Publisher) {

    fun start() {
        // Start the listener, to listen from brokers
        PublisherHandler(this.publisher).start()

        try {
            for (busID in this.publisher.topics) {
                for (stigma in BusProvider.readBusPositions(busID)) {
                    TimeUnit.SECONDS.sleep(3)
                    stigma.topic = busID
                    push(busID, stigma)
                    println(this.publisher.toString() + " Got data from sensor -> " + busID + ": " + stigma)
                }
            }
        } catch (err: Exception) {
            err.printStackTrace()
        }

    }

    private fun push(topic: Int, value: Stigma) {
        for (broker in this.publisher.brokers) {
            println("Checking $broker")
            if ((topic.toString()).hashCode() > broker.hash) {
                sendToBroker(broker, value)
                break
            }
        }
    }

    private fun sendToBroker(broker: Broker, stigma: Stigma) {
        try {
            val requestSocket: Socket = Socket(InetAddress.getByName(broker.ip), broker.port)
            val out: ObjectOutputStream
            out = ObjectOutputStream(requestSocket.getOutputStream())
            val toSend = Wrapper<Stigma>()
            toSend.data = stigma
            println(this.publisher.toString() + " Sent -> " + toSend)
            out.writeUnshared(toSend)
            out.flush()
        } catch (err: Exception) {
            System.err.println(this.publisher.toString() + " Tried to connect to -> " + broker.toString() + " But was down.")
        }

    }

    fun addTopic(busLineID: Int): PublisherEntity {
        this.publisher.addTopic(busLineID)
        return this
    }

}
