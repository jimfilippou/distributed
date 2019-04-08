import Models.Broker;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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
                System.out.println(this.broker.toString() + " Received -> " + in.readUTF());
                in.close();
                out.close();
                connection.close();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

    }

}
