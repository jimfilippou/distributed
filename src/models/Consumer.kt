package models

import java.io.Serializable
import java.util.ArrayList

class Consumer(val ip: String, val port: Int?) : Serializable {

    val interests: MutableList<Int> = ArrayList()

    fun addInterest(topic: Int) {
        this.interests.add(topic)
    }

    override fun toString(): String {
        return "Consumer ${this.ip}:${this.port}"
    }

    companion object {
        private const val serialVersionUID = 578515438738407941L
    }

}
