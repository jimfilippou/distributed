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
        PublisherHandler(publisher).start()

        // Supply data once
        publisher.data = BusProvider.readBusPositions(publisher.topics.toIntArray())

        // Distribute data for each Bus synchronously
        while (publisher.data.isNotEmpty()) {
            for (key in publisher.data.keys) {
                TimeUnit.SECONDS.sleep(2)
                val stigma: Stigma = publisher.data[key]!!.poll()
                stigma.topic = key
                if (!push(key, stigma)) {
                    println("$publisher Got data from sensor -> $key: $stigma")
                }
            }
        }

        // Bus got lost in a black hole
        println("$publisher ran out of data!")

    }

    private fun push(topic: Int, value: Stigma): Boolean {
        for (broker in this.publisher.brokers) {
            if ((topic.toString()).hashCode() >= broker.hash) {
                sendToBroker(broker, value)
                return true
            }
        }
        return false
    }

    private fun sendToBroker(broker: Broker, stigma: Stigma) {
        try {
            val requestSocket = Socket(InetAddress.getByName(broker.ip), broker.port)
            val out: ObjectOutputStream
            out = ObjectOutputStream(requestSocket.getOutputStream())
            val toSend = Wrapper<Stigma>()
            toSend.data = stigma
            println("$publisher Sent -> $toSend to $broker")
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
