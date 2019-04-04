import Helpers.BrokerProvider;
import Models.Broker;
import Models.Publisher;

import java.io.*;
import java.lang.*;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        HashMap<String, Broker> sharedHashmap = new HashMap<String, Broker>();
        for (Broker broker: BrokerProvider.fetchBrokers()){
            sharedHashmap.put(broker.getHash(), broker);
        }

        for (Broker broker : BrokerProvider.fetchBrokers()) {
            broker.setHashes(sharedHashmap);
            new BrokerThread(broker).start();
        }

        Publisher publisher = new Publisher("172.16.2.21", 9090);
        new PublisherThread(publisher).start();

    }
}
