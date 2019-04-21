package entities

import handlers.BrokerHandler
import helpers.BrokerProvider
import models.Broker
import models.Publisher
import models.Wrapper

import java.io.*
import java.net.*

class BrokerEntity(private val broker: Broker) {

    fun startSender() {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Send hash to other brokers
        for (broker in BrokerProvider.fetchBrokers()) {
            sendMyHash(broker)
        }

        // Send to every publisher, my hash along with myself
        for (publisher in this.broker.registeredPublishers) {
            sendMyHash(publisher)
        }
    }

    private fun sendMyHash(broker: Broker) {
        socketSend(broker.ip, broker.port, broker)
    }

    private fun sendMyHash(publisher: Publisher) {
        socketSend(publisher.ip, publisher.port, publisher)
    }

    private fun socketSend(ip: String, port: Int, `object`: Any) {
        try {
            val requestSocket = Socket(InetAddress.getByName(ip), port)
            val out: ObjectOutputStream
            out = ObjectOutputStream(requestSocket.getOutputStream())
            val toSend = Wrapper<Broker>()
            toSend.data = this.broker
            println(this.broker.toString() + " Sent -> " + this.broker.hash)
            out.writeUnshared(toSend)
            out.flush()
        } catch (err: Exception) {
            System.err.println(this.broker.toString() + " Tried to connect to -> " + `object` + " But was down.")
        }

    }

    fun startServer() {
        BrokerHandler(this.broker).start()
    }

}
