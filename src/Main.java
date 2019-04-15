import Helpers.BrokerProvider;
import Models.Broker;
import Models.Consumer;
import Models.Publisher;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        // ifconfig | grep "inet " | grep -v 127.0.0.1 | awk '{print $2}'
        String IP = "192.168.1.4";

        if (args[0].equals("brokers")) {
            List<BrokerEntity> brokers = new ArrayList<>();
            // Create brokers
            for (Broker broker : BrokerProvider.fetchBrokers()) {
                broker.addPublisher(new Publisher(IP, 9090));
                BrokerEntity x = new BrokerEntity(broker);
                x.startServer();
                brokers.add(x);
            }
            // Start brokers
            for (BrokerEntity brokerEntity : brokers) {
                brokerEntity.startSender();
            }
        } else if (args[0].equals("publishers")) {
            Publisher publisher = new Publisher(IP, 9090);
            new PublisherEntity(publisher)
                    .addTopic(821)
                    .start();
        } else {
            Consumer consumer = new Consumer(IP, 9091);
            ConsumerEntity consumerEntity = new ConsumerEntity(consumer);
            consumerEntity.register(new Broker(IP, 8081), 821);
            consumerEntity.startListening();
        }
    }
}
