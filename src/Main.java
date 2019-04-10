import Helpers.BrokerProvider;
import Models.Broker;
import Models.Publisher;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        List<BrokerEntity> brokers = new ArrayList<>();

        // Create brokers
        for (Broker broker : BrokerProvider.fetchBrokers()) {
            broker.addPublisher(new Publisher("172.16.2.49", 9090));
            BrokerEntity x = new BrokerEntity(broker);
            x.startServer();
            brokers.add(x);
        }

        // Start brokers
        for (BrokerEntity brokerEntity : brokers) {
            brokerEntity.startSender();
        }

    }
}
