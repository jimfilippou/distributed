import Models.Consumer;
import Models.Wrapper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsumerHandler extends Thread {

    private Consumer consumer;

    ConsumerHandler(Consumer consumer) {
        this.consumer = consumer;
    }

    public void run() {
        ServerSocket provider;
        Socket connection;
        InetAddress address;
        try {
            address = InetAddress.getByName(this.consumer.getIP());
            provider = new ServerSocket(this.consumer.getPort(), 50, address);
            while (true) {
                connection = provider.accept();
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());

                Wrapper incoming = (Wrapper) in.readUnshared();
                System.out.println(this.consumer.toString() + " Received -> " + incoming);

                in.close();
                out.close();
                connection.close();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
