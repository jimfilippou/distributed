import Helpers.BrokerProvider;
import Models.Broker;
import Models.Publisher;
import Models.Wrapper;

import java.io.*;
import java.net.*;

class BrokerEntity {

    private Broker broker;

    BrokerEntity(Broker broker) {
        this.broker = broker;
    }

    void startSender() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send hash to other brokers
        for (Broker broker : BrokerProvider.fetchBrokers()) {
            sendMyHash((BrokerOrPublisher) broker);
        }

        // Send to every publisher, my hash along with myself
        for (Publisher publisher : this.broker.getRegisteredPublishers()) {
            sendMyHash((BrokerOrPublisher) publisher);
        }
    }

    private void sendMyHash(BrokerOrPublisher entity) {
        try {
            Socket requestSocket;
            ObjectOutputStream out;
            requestSocket = new Socket(InetAddress.getByName(entity.getIP()), entity.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            Wrapper<String> toSend = new Wrapper<>();
            toSend.data = this.broker.getHash();
            System.out.println(this.broker.toString() + " Sent -> " + this.broker.getHash());
            out.writeUnshared(toSend);
            out.flush();
        } catch (Exception err) {
            System.err.println(this.broker.toString() + " Tried to connect to -> " + entity.toString() + " But was down.");
        }
    }

    interface BrokerOrPublisher {
        String getIP();
        Integer getPort();
    }

    void startServer() {
        new BrokerHandler(this.broker).start();
    }

}
