import entities.BrokerEntity
import entities.ConsumerEntity
import entities.PublisherEntity
import helpers.ArgParser
import helpers.BrokerProvider
import models.Broker
import models.Consumer
import models.Publisher

import java.util.ArrayList


object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        // ifconfig | grep "inet " | grep -v 127.0.0.1 | awk '{print $2}'
        val IP = "10.101.67.186"

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
                val publisher = Publisher(IP, 9090)
                PublisherEntity(publisher)
                        .addTopic(821)
                        .addTopic(804)
                        .start()
            }
            "consumers" -> {
                val consumer = Consumer(IP, 9091)
                val consumerEntity = ConsumerEntity(consumer)
                consumerEntity.register(Broker(IP, 8081), 821)
                consumerEntity.startListening()
            }
            else -> println("Not recognized command.")
        }
    }
}
