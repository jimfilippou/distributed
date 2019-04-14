import Models.Broker;
import Models.Consumer;
import Models.Stigma;
import Models.Wrapper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class BrokerHandler extends Thread {

    private Broker broker;

    BrokerHandler(Broker broker) {
        this.broker = broker;
    }

    public void run() {
        ServerSocket providerSocket;
        Socket connection;
        InetAddress addr;
        try {
            addr = InetAddress.getByName(this.broker.getIP());
            providerSocket = new ServerSocket(this.broker.getPort(), 50, addr);
            while (true) {
                connection = providerSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());

                Wrapper incoming = (Wrapper) in.readUnshared();

                if (incoming.data instanceof HashMap) {
                    Map.Entry entry = (Map.Entry) ((HashMap) incoming.data).entrySet().iterator().next();
                    Consumer value = (Consumer) entry.getValue();
                    this.broker.getRegisteredConsumers().add(value);
                    System.out.println(this.broker + " Received registration event from " + value.toString());
                } else {
                    // Received stigma, send this back to consumers
                    System.out.println(this.broker.toString() + " Received -> " + incoming.data);
                    for (Consumer consumer : this.broker.getRegisteredConsumers()) {
                        Integer incomingTopic = ((Stigma) (incoming.data)).getTopic();
                        if (consumer.getInterests().indexOf(incomingTopic) != -1) {
                            try {
                                Socket requestSocket;
                                ObjectOutputStream outWriter;
                                requestSocket = new Socket(InetAddress.getByName(consumer.getIP()), consumer.getPort());
                                outWriter = new ObjectOutputStream(requestSocket.getOutputStream());
                                Wrapper<Stigma> toSend = new Wrapper<>();
                                toSend.data = ((Stigma) (incoming.data));
                                System.out.println(this.broker.toString() + " Sent -> " + ((Stigma) (incoming.data)).toString());
                                outWriter.writeUnshared(toSend);
                                outWriter.flush();
                            } catch (Exception err) {
                                System.err.println(this.broker.toString() + " Tried to connect to -> " + consumer.toString() + " But was down.");
                            }
                        }
                    }
                }

                in.close();
                out.close();
                connection.close();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

    }

}
