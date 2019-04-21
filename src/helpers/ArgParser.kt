package helpers

import models.Broker
import models.Publisher
import java.util.*

object ArgParser {
    @Throws(IllegalArgumentException::class)
    fun fetchPublishersFromCommandLine(args: Array<String>, returnType: Any): List<Any> {
        val entities = ArrayList<Any>()
        var position = -1
        for (i in args.indices) {
            if (args[i] == "-p") {
                position = i
            }
        }
        if (position == -1)
            throw IllegalArgumentException()
        try {
            while (args[++position] != null) {
                val data = args[position].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var entity: Any
                when (returnType) {
                    "publisher" -> entity = Publisher(data[0], Integer.parseInt(data[1]))
                    "broker" -> entity = Broker(data[0], Integer.parseInt(data[1]))
                    else -> entity = Object()
                }
                entities.add(entity)
            }
            // This line never executes, however compiler warned me.
            throw IndexOutOfBoundsException()
        } catch (err: IndexOutOfBoundsException) {
            return entities
        }

    }
}
