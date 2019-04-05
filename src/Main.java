import Helpers.BrokerProvider;
import Models.Broker;
import Models.Publisher;

import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    static final String myIP = "172.16.2.21";

    public static void main(String[] args) throws FileNotFoundException {

//        HashMap<String, Broker> sharedHashmap = new HashMap<String, Broker>();
//        for (Broker broker: BrokerProvider.fetchBrokers()){
//            sharedHashmap.put(broker.getHash(), broker);
//        }
//        broker.setHashes(sharedHashmap);

        List<BrokerEntity> brokers = new ArrayList<BrokerEntity>();

        // Create brokers
        for (Broker broker : BrokerProvider.fetchBrokers()) {
            BrokerEntity x = new BrokerEntity(broker);
            x.start();
            brokers.add(x);
        }

        // Start brokers
        for(BrokerEntity brokerEntity: brokers){
            brokerEntity.startSender();
        }




//        Publisher publisher = new Publisher("172.16.2.21", 9090);
//        new PublisherThread(publisher).start();

    }
}
