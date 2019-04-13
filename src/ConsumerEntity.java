import Models.Broker;
import Models.Consumer;
import Models.Wrapper;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class ConsumerEntity {

    private Consumer consumer;

    ConsumerEntity(Consumer consumer) {
        this.consumer = consumer;
    }

    public ConsumerEntity register(Broker broker, Integer topic) {
        try {
            Socket requestSocket;
            ObjectOutputStream out;
            requestSocket = new Socket(InetAddress.getByName(broker.getIP()), broker.getPort());
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            Wrapper<HashMap<Integer, Consumer>> toSend = new Wrapper<>();
            toSend.data = new HashMap<>();
            toSend.data.put(topic, this.consumer);
            System.out.println(this.consumer.toString() + " Sent registration event -> " + broker.toString());
            out.writeUnshared(toSend);
            out.flush();
            this.consumer.addInterest(topic);
        } catch (Exception err) {
            System.err.println(this.consumer.toString() + " Tried to connect to -> " + broker.toString() + " But was down.");
        }
        return this;
    }

    public void disconnect(Broker broker, Integer topic) {

    }

    void startListening() {
        new ConsumerHandler(this.consumer)
                .run();
    }

}
