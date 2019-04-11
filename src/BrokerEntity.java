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
            sendMyHash(broker);
        }

        // Send to every publisher, my hash along with myself
        for (Publisher publisher : this.broker.getRegisteredPublishers()) {
            sendMyHash(publisher);
        }
    }

    private void sendMyHash(Broker broker) {
        socketSend(broker.getIP(), broker.getPort(), broker);
    }

    private void sendMyHash(Publisher publisher) {
        socketSend(publisher.getIP(), publisher.getPort(), publisher);
    }

    private void socketSend(String ip, int port, Object object) {
        try {
            Socket requestSocket;
            ObjectOutputStream out;
            requestSocket = new Socket(InetAddress.getByName(ip), port);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            Wrapper<Broker> toSend = new Wrapper<>();
            toSend.data = this.broker;
            System.out.println(this.broker.toString() + " Sent -> " + this.broker.getHash());
            out.writeUnshared(toSend);
            out.flush();
        } catch (Exception err) {
            System.err.println(this.broker.toString() + " Tried to connect to -> " + object + " But was down.");
        }
    }

    void startServer() {
        new BrokerHandler(this.broker).start();
    }

}
