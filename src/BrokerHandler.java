import Helpers.Hash;
import Models.Broker;
import Models.Consumer;
import Models.Wrapper;

import java.io.EOFException;
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
                System.out.println(this.broker.toString() + " Received -> " + incoming);

                if (incoming.data instanceof HashMap) {
                    Map.Entry entry = (Map.Entry) ((HashMap) incoming.data).entrySet().iterator().next();
                    Consumer value = (Consumer) entry.getValue();
                    this.broker.getRegisteredConsumers().add(value);
                    System.out.println(this.broker + " Received registration event from " + value.toString());
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
