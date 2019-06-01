package helpers

import gr.aueb.distributedapp.models.Stigma

import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.collections.HashMap

object BusProvider {
    @Throws(FileNotFoundException::class)
    fun readBusPositions(topics: IntArray): HashMap<Int, Queue<Stigma>> {
        val response: HashMap<Int, Queue<Stigma>> = HashMap()
        val input = Scanner(File("/Users/jimfilippou/Projects/distributed/src/data/busPositions.txt"))
        input.useDelimiter("-\n")
        for (topic in topics){
            response[topic] = LinkedList<Stigma>()
        }
        while (input.hasNextLine()) {
            val data = input.nextLine().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val busId = Integer.parseInt(data[0])
            val x = java.lang.Double.valueOf(data[3])
            val y = java.lang.Double.valueOf(data[4])
            val timestamp = data[5]
            if (topics.indexOf(busId) != -1) {
                response[busId]!!.add(Stigma(x, y, timestamp))
            }
        }
        return response
    }
}
