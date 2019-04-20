package models

import java.io.Serializable
import java.util.*

class Broker(val ip: String, val port: Int) : Serializable {

    val hash: Int = (this.ip + port.toString()).hashCode() % 100
    val registeredPublishers = ArrayList<Publisher>()
    val registeredConsumers: MutableList<Consumer> = ArrayList()
    val data = HashMap<Int, Queue<Stigma>>()

    fun addPublisher(publisher: Publisher) {
        this.registeredPublishers.add(publisher)
    }

    override fun toString(): String {
        return "Broker " + this.ip + ":" + this.port
    }

    companion object {
        private const val serialVersionUID = 578515438738407941L
    }

}
