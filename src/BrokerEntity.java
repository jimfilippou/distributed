import Helpers.BrokerProvider;
import Models.Broker;

import java.io.*;
import java.net.*;

public class BrokerEntity {

    private Broker broker;

    public BrokerEntity(Broker broker) {
        this.broker = broker;
    }

    private void startSender() {
        for (Broker broker : BrokerProvider.fetchBrokers()) {
            BufferedWriter out;
            try (Socket s = new Socket(broker.getIP(), broker.getPort())) {
                out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                out.write(broker.getHash());
                out.newLine();
                out.flush();
            } catch (ConnectException err) {
                // Connection failed because not all brokers on the txt are up
            } catch (IOException e) {
                e.printStackTrace();
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
            InetAddress addr = null;
            try {
                addr = InetAddress.getByName(this.broker.getIP());
                providerSocket = new ServerSocket(this.broker.getPort(), 50, addr);
                Socket s = null;
                s = providerSocket.accept();
                while (true) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            } catch (Exception err) {
                err.printStackTrace();
            }

        }

    }


    public void start() {
        System.out.println("Broker " + broker.getIP() + ":" + broker.getPort() + " -> Started server.");
        this.startServer();
        System.out.println("Broker " + broker.getIP() + ":" + broker.getPort() + " -> Started client.");
        this.startSender();
    }
}
