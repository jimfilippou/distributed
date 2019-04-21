package entities

import handlers.ConsumerHandler
import models.Broker
import models.Consumer
import models.Wrapper

import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket

class ConsumerEntity(private val consumer: Consumer) {

    fun register(broker: Broker, topic: Int) {
        try {
            val requestSocket = Socket(InetAddress.getByName(broker.ip), broker.port)
            val out: ObjectOutputStream
            out = ObjectOutputStream(requestSocket.getOutputStream())
            println("Adding $topic to the interests list.")
            consumer.addInterest(topic)
            val toSend = Wrapper<Consumer>()
            toSend.data = consumer
            println("$consumer Sent registration event -> $broker")
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
