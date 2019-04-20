package handlers

import models.Broker
import models.Publisher
import models.Wrapper

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class PublisherHandler(private val publisher: Publisher) : Thread() {

    override fun run() {
        val providerSocket: ServerSocket
        var connection: Socket
        val addr: InetAddress
        try {
            addr = InetAddress.getByName(this.publisher.ip)
            providerSocket = ServerSocket(this.publisher.port, 50, addr)
            println(this.publisher.toString() + " Server started")
            while (true) {
                connection = providerSocket.accept()
                val out = ObjectOutputStream(connection.getOutputStream())
                val `in` = ObjectInputStream(connection.getInputStream())
                val incoming = `in`.readObject() as Wrapper<*>
                println(this.publisher.toString() + " Received -> " + incoming.data!!.toString())
                synchronized(this) {
                    if (incoming.data is Broker) {
                        this.publisher.brokers.add(incoming.data as Broker)
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
