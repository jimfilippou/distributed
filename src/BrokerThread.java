import Helpers.BrokerProvider;
import Models.Broker;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerThread implements Runnable {

    private Broker broker;
    private Thread t;

    public BrokerThread(Broker broker) {
        this.broker = broker;
    }

    private void startSender() {
        (new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(500);
                    for (Broker broker : BrokerProvider.fetchBrokers()) {
                        if (broker.getIP().equals("172.16.2.21")) continue;
                        BufferedWriter out;
                        try (Socket s = new Socket(broker.getIP(), broker.getPort())) {
                            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                            out.write(this.broker.getHash());
                            out.newLine();
                            out.flush();
                        } catch (ConnectException err) {
                            // Connection failed because not all brokers on the txt are up
                        }
                    }
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        })).start();
    }

    private void startServer() {
        (new Thread(() -> {
            ServerSocket providerSocket = null;
            try {
                InetAddress addr = InetAddress.getByName(this.broker.getIP());
                providerSocket = new ServerSocket(this.broker.getPort(), 50, addr);
                System.out.println("BrokerThread started at:" + addr + ":" + this.broker.getPort());
                Socket s = providerSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        })).start();
    }

    @Override
    public void run() {
        if (broker.getIP().equals("172.16.2.21")) {
            System.out.println("Starting server...");
            this.startServer();
            System.out.println("Starting sender...");
            this.startSender();
        } else {
            System.out.println("Starting sender...");
            this.startSender();
        }

    }

    public void start() {
        String threadname = "THREAD" + this.broker.getPort();
        Thread thread = this.t;
        if (thread == null) {
            thread = new Thread(this, threadname);
            thread.start();
        }
    }
}
