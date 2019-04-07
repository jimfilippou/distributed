import Helpers.BrokerProvider;
import Models.Broker;

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

        ClientHandler(Broker broker) {
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

    void start() {
        this.startServer();
    }
}
