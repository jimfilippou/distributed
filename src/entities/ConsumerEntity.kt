package entities

import handlers.ConsumerHandler
import models.Broker
import models.Consumer
import models.Wrapper

import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket
import java.util.HashMap

class ConsumerEntity(private val consumer: Consumer) {

    fun register(broker: Broker, topic: Int?) {
        try {
            val requestSocket: Socket = Socket(InetAddress.getByName(broker.ip), broker.port)
            val out: ObjectOutputStream
            out = ObjectOutputStream(requestSocket.getOutputStream())
            println("Adding " + topic!!.toString() + " to the interests list.")
            this.consumer.addInterest(topic)
            val toSend = Wrapper<HashMap<Int, Consumer>>()
            toSend.data = HashMap()
            toSend.data!![topic] = this.consumer
            println(this.consumer.toString() + " Sent registration event -> " + broker.toString())
            out.writeUnshared(toSend)
            out.flush()
        } catch (err: Exception) {
            System.err.println(this.consumer.toString() + " Tried to connect to -> " + broker.toString() + " But was down.")
        }
    }

    fun disconnect(broker: Broker, topic: Int?) {

    }

    fun startListening() {
        ConsumerHandler(this.consumer)
                .run()
    }

}
