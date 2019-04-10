import Helpers.BrokerProvider;
import Models.Broker;
import Models.Publisher;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        if (args[0].equals("brokers")) {
            List<BrokerEntity> brokers = new ArrayList<>();
            // Create brokers
            for (Broker broker : BrokerProvider.fetchBrokers()) {
                broker.addPublisher(new Publisher("192.168.1.4", 9090));
                BrokerEntity x = new BrokerEntity(broker);
                x.startServer();
                brokers.add(x);
            }
            // Start brokers
            for (BrokerEntity brokerEntity : brokers) {
                brokerEntity.startSender();
            }
        } else if (args[0].equals("publishers")) {
            new PublisherEntity(new Publisher("192.168.1.4", 9090)).addTopic(821).start();
        }


    }
}
