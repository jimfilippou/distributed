package models

import java.io.Serializable
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class Stigma(private val lat: Double, private val lng: Double, timestamp: String) : Serializable {
    var topic: Int? = null
    private var timestamp: Timestamp? = null

    init {
        this.setTimestamp(timestamp)
    }

    private fun setTimestamp(time: String) {
        try {
            val dateFormat = SimpleDateFormat(
                    "MMM  d yyyy hh:mm:ss:SSSa", Locale.ENGLISH
            )
            val parsedDate = dateFormat.parse(time)
            this.timestamp = Timestamp(parsedDate.time)
        } catch (err: ParseException) {
            err.printStackTrace()
        }

    }

    override fun toString(): String {
        return "Stigma ${this.lat}, ${this.lng}"
    }

    companion object {
        private const val serialVersionUID = 578515438738407941L
    }
}
