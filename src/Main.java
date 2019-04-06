import Helpers.BrokerProvider;
import Models.Broker;
import Models.Publisher;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

//        HashMap<String, Broker> sharedHashmap = new HashMap<String, Broker>();
//        for (Broker broker: BrokerProvider.fetchBrokers()){
//            sharedHashmap.put(broker.getHash(), broker);
//        }
//        broker.setHashes(sharedHashmap);

//        List<BrokerEntity> brokers = new ArrayList<BrokerEntity>();

        // Create brokers
//        for (Broker broker : BrokerProvider.fetchBrokers()) {
//            BrokerEntity x = new BrokerEntity(broker);
//            x.start();
//            brokers.add(x);
//        }

        // Start brokers
//        for (BrokerEntity brokerEntity : brokers) {
//            brokerEntity.startSender();
//        }

        Publisher publisher = new Publisher("172.16.2.21", 9090);
        PublisherEntity publisherEntity = new PublisherEntity(publisher);
        publisherEntity.addTopic(821);
        publisherEntity.addTopic(817);
        publisherEntity.start();

    }
}
