import Helpers.BrokerProvider;
import Models.Broker;

import java.io.*;
import java.net.*;

public class BrokerEntity {

    private Broker broker;

    public BrokerEntity(Broker broker) {
        this.broker = broker;
    }

    public void startSender() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Broker broker : BrokerProvider.fetchBrokers()) {
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
    }

    private void startServer() {
        new ClientHandler(this.broker).start();
    }


    class ClientHandler extends Thread {

        private Broker broker;

        public ClientHandler(Broker broker) {
            this.broker = broker;
        }

        public void run() {
            ServerSocket providerSocket = null;
            Socket connection = null;
            InetAddress addr = null;
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

    public void start() {
        this.startServer();
    }
}
