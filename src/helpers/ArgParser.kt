package helpers

import models.Publisher

import java.util.ArrayList

object ArgParser {
    @Throws(IllegalArgumentException::class)
    fun fetchPublishersFromCommandLine(args: Array<String>): List<Publisher> {
        val publishers = ArrayList<Publisher>()
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
                val publisher = Publisher(data[0], Integer.parseInt(data[1]))
                publishers.add(publisher)
            }
            // This line never executes, however compiler warned me.
            throw IndexOutOfBoundsException()
        } catch (err: IndexOutOfBoundsException) {
            return publishers
        }

    }
}
