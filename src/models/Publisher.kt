package models

import java.io.Serializable
import java.util.ArrayList

class Publisher(val ip: String, val port: Int) : Serializable {

    val brokers: MutableList<Broker> = ArrayList()

    val topics = ArrayList<Int>()

    fun addTopic(topic: Int) {
        this.topics.add(topic)
    }

    override fun toString(): String {
        return "Publisher " + this.ip + ":" + this.port
    }

    companion object {
        private const val serialVersionUID = 578515438738407941L
    }

}
