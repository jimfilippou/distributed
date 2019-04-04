import Helpers.BrokerProvider;
import Models.Broker;

import java.io.*;
import java.net.*;

public class BrokerThread {

    private Broker broker;
    private Thread t;

    public BrokerThread(Broker broker) {
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

        ServerSocket providerSocket = null;
        BufferedReader in = null;
        try {
            InetAddress addr = InetAddress.getByName(this.broker.getIP());
            providerSocket = new ServerSocket(this.broker.getPort(), 50, addr);
            System.out.println("BrokerThread started at:" + addr + ":" + this.broker.getPort());


            // create a new thread object
            Thread t = new ClientHandler(providerSocket);

            // Invoking the start() method
            t.start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // ClientHandler class
    class ClientHandler extends Thread {
        final ServerSocket s;

        // Constructor
        public ClientHandler(ServerSocket s) {
            this.s = s;
        }

        public void run() {

            Socket s = null;
            try {
                s = this.s.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {

                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    // receive the answer from client


                    String line = null;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    public void start() {
        System.out.println("Starting server...");
        this.startServer();
        System.out.println("Starting sender...");
        this.startSender();
    }
}
