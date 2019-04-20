package handlers

import models.Consumer
import models.Wrapper

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class ConsumerHandler(private val consumer: Consumer) : Thread() {

    override fun run() {
        val provider: ServerSocket
        var connection: Socket
        val address: InetAddress
        try {
            address = InetAddress.getByName(this.consumer.ip)
            provider = ServerSocket(this.consumer.port!!, 50, address)
            while (true) {
                connection = provider.accept()
                val out = ObjectOutputStream(connection.getOutputStream())
                val `in` = ObjectInputStream(connection.getInputStream())

                val incoming = `in`.readUnshared() as Wrapper<*>
                println(this.consumer.toString() + " Received -> " + incoming)

                `in`.close()
                out.close()
                connection.close()
            }
        } catch (err: Exception) {
            err.printStackTrace()
        }

    }

}
