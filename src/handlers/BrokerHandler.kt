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
import java.util.HashMap

class BrokerHandler(private val broker: Broker) : Thread() {

    override fun run() {
        val providerSocket: ServerSocket
        var connection: Socket
        val addr: InetAddress
        try {
            addr = InetAddress.getByName(this.broker.ip)
            providerSocket = ServerSocket(this.broker.port, 50, addr)
            while (true) {
                connection = providerSocket.accept()
                val out = ObjectOutputStream(connection.getOutputStream())
                val `in` = ObjectInputStream(connection.getInputStream())

                val incoming = `in`.readUnshared() as Wrapper<*>

                if (incoming.data is HashMap<*, *>) {
                    val entry = (incoming.data as HashMap<*, *>).entries.iterator().next()
                    val value = entry.value as Consumer
                    this.broker.registeredConsumers.add(value)
                    println(this.broker.toString() + " Received registration event from " + value.toString())
                } else {
                    // Received stigma, send this back to consumers
                    println(this.broker.toString() + " Received -> " + incoming.data)
                    for (consumer in this.broker.registeredConsumers) {
                        val incomingTopic = (incoming.data as Stigma).topic
                        if (consumer.interests.indexOf(incomingTopic) != -1) {
                            try {
                                val requestSocket: Socket = Socket(InetAddress.getByName(consumer.ip)!!, consumer.port!!)
                                val outWriter: ObjectOutputStream = ObjectOutputStream(requestSocket.getOutputStream())
                                val toSend = Wrapper<Stigma>()
                                toSend.data = incoming.data as Stigma
                                println(this.broker.toString() + " Sent -> " + (incoming.data as Stigma).toString())
                                outWriter.writeUnshared(toSend)
                                outWriter.flush()
                            } catch (err: Exception) {
                                System.err.println(this.broker.toString() + " Tried to connect to -> " + consumer.toString() + " But was down.")
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
