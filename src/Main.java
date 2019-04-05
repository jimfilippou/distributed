import Helpers.BrokerProvider;
import Models.Broker;
import Models.Publisher;

import java.io.*;
import java.lang.*;


public class Main {

    static final String myIP = "172.16.2.21";

    public static void main(String[] args) throws FileNotFoundException {

//        HashMap<String, Broker> sharedHashmap = new HashMap<String, Broker>();
//        for (Broker broker: BrokerProvider.fetchBrokers()){
//            sharedHashmap.put(broker.getHash(), broker);
//        }
//        broker.setHashes(sharedHashmap);

        for (Broker broker : BrokerProvider.fetchBrokers()) {
            new BrokerEntity(broker).start();
        }



//        Publisher publisher = new Publisher("172.16.2.21", 9090);
//        new PublisherThread(publisher).start();

    }
}
