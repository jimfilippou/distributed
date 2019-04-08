import Helpers.BrokerProvider;
import Models.Broker;
import Models.Publisher;

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
            sendMyHash(broker);
        }

        // Send to every publisher, my hash along with myself
        for (Publisher publisher : this.broker.getRegisteredPublishers()) {
            sendMyHash(publisher);
        }
    }

    private void sendMyHash(Publisher publisher) {
        try {
            Socket requestSocket;
            ObjectOutputStream out;
            requestSocket = new Socket(InetAddress.getByName(publisher.getIP()), publisher.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            System.out.println(this.broker.toString() + " Sent -> " + this.broker.getHash());
            out.writeUnshared(this.broker);
            out.flush();
        } catch (Exception err) {
            System.err.println(this.broker.toString() + " Tried to connect to -> " + publisher.toString() + " But was down.");
        }
    }

    private void sendMyHash(Broker broker) {
        try {
            Socket requestSocket;
            ObjectOutputStream out;
            requestSocket = new Socket(InetAddress.getByName(broker.getIP()), broker.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            System.out.println(this.broker.toString() + " Sent -> " + this.broker.getHash());
            out.writeUTF(this.broker.getHash());
            out.flush();
        } catch (Exception err) {
            System.err.println(this.broker.toString() + " Tried to connect to -> " + broker.toString() + " But was down.");
        }
    }

    void startServer() {
        new BrokerHandler(this.broker).start();
    }

}
