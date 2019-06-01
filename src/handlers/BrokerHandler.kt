package handlers

import gr.aueb.distributedapp.models.Wrapper
import gr.aueb.distributedapp.models.Stigma
import gr.aueb.distributedapp.models.Broker
import gr.aueb.distributedapp.models.Consumer

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

                val output = ObjectOutputStream(connection.getOutputStream())
                val input = ObjectInputStream(connection.getInputStream())

                val incoming: Wrapper<*> = input.readUnshared() as Wrapper<*>

                // Registration event from consumer
                when {
                    incoming.data is Consumer -> {
                        this.broker.registeredConsumers.add((incoming.data) as Consumer)
                        println("$broker Received registration event from ${incoming.data}")
                    }
                    incoming.data is Broker -> println("Received from ${incoming.data}")
                    else -> {
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
                }
                input.close()
                output.close()
                connection.close()
            }
        } catch (err: Exception) {
            err.printStackTrace()
        }
    }

}
