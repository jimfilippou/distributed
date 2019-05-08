package entities

import handlers.ConsumerHandler
import helpers.BrokerProvider
import models.Broker
import models.Consumer
import models.Wrapper

import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket

class ConsumerEntity(private val consumer: Consumer) {

    fun register(topic: Int) {

        for (brokerProvided in BrokerProvider.fetchBrokers()) {
            if ((topic.toString()).hashCode() >= brokerProvided.hash) {
                try {
                    val requestSocket = Socket(InetAddress.getByName(brokerProvided.ip), brokerProvided.port)
                    val out: ObjectOutputStream
                    out = ObjectOutputStream(requestSocket.getOutputStream())
                    println("Adding $topic to the interests list.")
                    consumer.addInterest(topic)
                    val toSend = Wrapper<Consumer>()
                    toSend.data = consumer
                    println("$consumer Sent registration event -> $brokerProvided")
                    out.writeUnshared(toSend)
                    out.flush()
                } catch (err: Exception) {
                    System.err.println("$consumer Tried to connect to -> $brokerProvided But was down.")
                }
            }
        }
    }

    fun startListening() {
        ConsumerHandler(this.consumer)
                .run()
    }

}
