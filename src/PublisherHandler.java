import Models.Broker;
import Models.Publisher;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PublisherHandler extends Thread {

    private Publisher publisher;

    PublisherHandler(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void run() {
        ServerSocket providerSocket;
        Socket connection;
        InetAddress addr;
        try {
            addr = InetAddress.getByName(this.publisher.getIP());
            providerSocket = new ServerSocket(this.publisher.getPort(), 50, addr);
            System.out.println(this.publisher.toString() + " Server started");
            while (true) {
                connection = providerSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                Broker object = (Broker) in.readObject();
                System.out.println(this.publisher.toString() + " Received -> " + object.toString());
                synchronized (this) {
                    this.publisher.getBrokers().add(object);
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
