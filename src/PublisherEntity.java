import Helpers.BusReader;
import Models.Broker;
import Models.Publisher;
import Models.Stigma;
import Models.Wrapper;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

class PublisherEntity {

    private Publisher publisher;

    PublisherEntity(Publisher publisher) {
        this.publisher = publisher;
    }

    void start() {
        // Start the listener, to listen from brokers
        new PublisherHandler(this.publisher).start();

        try {
            for (int busID : this.publisher.getTopics()) {
                for (Stigma stigma : BusReader.readBusPositions(busID)) {
                    TimeUnit.SECONDS.sleep(3);
                    push(busID, stigma);
                    System.out.println(this.publisher.toString() + " Got data from sensor -> " + busID + ": " + stigma);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void push(int topic, Stigma value) {
        for (Broker broker : this.publisher.getBrokers()) {
            System.out.println("Checking " + broker.toString());
            if (publisher.hashThis(String.valueOf(topic)).compareTo(broker.getHash()) > 0) {
                sendToBroker(broker, value);
            }
        }
    }

    private void sendToBroker(Broker broker, Stigma stigma){
        try {
            Socket requestSocket;
            ObjectOutputStream out;
            requestSocket = new Socket(InetAddress.getByName(broker.getIP()), broker.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            Wrapper<Stigma> toSend = new Wrapper<>();
            toSend.data = stigma;
            System.out.println(this.publisher.toString() + " Sent -> " + toSend);
            out.writeUnshared(toSend);
            out.flush();
        } catch (Exception err) {
            System.err.println(this.publisher.toString() + " Tried to connect to -> " + broker.toString() + " But was down.");
        }
    }

    PublisherEntity addTopic(int busLineID) {
        this.publisher.addTopic(busLineID);
        return this;
    }

}
