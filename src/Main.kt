// Models
import gr.aueb.distributedapp.models.Consumer
import gr.aueb.distributedapp.models.Publisher

// Entities
import entities.BrokerEntity
import entities.ConsumerEntity
import entities.PublisherEntity

// Helpers
import helpers.ArgParser
import helpers.BrokerProvider


import java.util.ArrayList


object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        // $ ~ ifconfig | grep "inet " | grep -v 127.0.0.1 | awk '{print $2}'
        val ip = "192.168.1.26"

        when (args[0]) {
            "brokers" -> {
                val brokers = ArrayList<BrokerEntity>()
                // Create brokers
                for (broker in BrokerProvider.fetchBrokers()) {
                    // Supply broker with available publishers
                    for (publisher in ArgParser.fetchPublishersFromCommandLine(args, "publisher")) {
                        broker.addPublisher(publisher as Publisher)
                    }
                    val x = BrokerEntity(broker)
                    x.startServer()
                    brokers.add(x)
                }
                // Start brokers
                for (brokerEntity in brokers) {
                    brokerEntity.startSender()
                }
            }
            "publishers" -> {
                val publisher = Publisher(ip, 9090)
                PublisherEntity(publisher)
                        .addTopic(821)
                        .addTopic(804)
                        .start()
            }
            "consumers" -> {
                val consumer = Consumer(ip, 9091)
                val consumerEntity = ConsumerEntity(consumer)
                consumerEntity.register(821)
                consumerEntity.startListening()
            }
            else -> println("Not recognized command.")
        }
    }
}
