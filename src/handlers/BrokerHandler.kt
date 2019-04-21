package handlers

import models.Broker
import models.Consumer
import models.Stigma
import models.Wrapper

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class BrokerHandler(private val broker: Broker) : Thread() {

    override fun run() {
        val providerSocket: ServerSocket
        var connection: Socket
        val address: InetAddress
        try {
            address = InetAddress.getByName(this.broker.ip)
            providerSocket = ServerSocket(this.broker.port, 50, address)
            while (true) {
                connection = providerSocket.accept()
                val out = ObjectOutputStream(connection.getOutputStream())
                val `in` = ObjectInputStream(connection.getInputStream())
                val incoming = `in`.readUnshared() as Wrapper<*>
                // Registration event from consumer
                if (incoming.data is Consumer) {
                    this.broker.registeredConsumers.add((incoming.data) as Consumer)
                    println("$broker Received registration event from ${incoming.data}")
                } else if (incoming.data is Broker) {
                    println("Received from ${incoming.data}")
                } else {
                    // Received stigma, send this back to consumers
                    val stigma: Stigma = incoming.data as Stigma
                    println("$broker Received -> $stigma")
                    for (consumer in this.broker.registeredConsumers) {
                        val incomingTopic = stigma.topic
                        val interested: Boolean = consumer.interests.indexOf(incomingTopic) != -1
                        if (interested) {
                            try {
                                val requestSocket = Socket(InetAddress.getByName(consumer.ip)!!, consumer.port!!)
                                val outWriter = ObjectOutputStream(requestSocket.getOutputStream())
                                val toSend = Wrapper<Stigma>()
                                toSend.data = incoming.data as Stigma
                                println("$broker Sent -> $stigma")
                                outWriter.writeUnshared(toSend)
                                outWriter.flush()
                            } catch (err: Exception) {
                                System.err.println("$broker Tried to connect to -> $consumer But was down.")
                            }

                        }
                    }
                }
                `in`.close()
                out.close()
                connection.close()
            }
        } catch (err: Exception) {
            err.printStackTrace()
        }
    }

}
