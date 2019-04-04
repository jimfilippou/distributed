import Models.Broker;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerThread implements Runnable {

    private Broker broker;
    private Thread t;

    public BrokerThread(Broker broker) {
        this.broker = broker;
    }

//    private void startSender() {
//        (new Thread(() -> {
//            try {
//                for (Broker broker : BrokerProvider.fetchBrokers()) {
//                    Socket s = new Socket(broker.getIP(), 9090);
//                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//                    out.write(broker.getHash());
//                    out.newLine();
//                    out.flush();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        })).start();
//    }

    private void startServer() {
        (new Thread(() -> {
            ServerSocket providerSocket = null;
            try {
                InetAddress addr = InetAddress.getByName(this.broker.getIP());
                providerSocket = new ServerSocket(this.broker.getPort(), 50, addr);
                System.out.println("BrokerThread started at:" + addr + ":" + this.broker.getPort());
                Socket s = providerSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//                ObjectOutputStream objectOutput = new ObjectOutputStream(s.getOutputStream());
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
        System.out.println("Starting server...");
        this.startServer();
//        System.out.println("Starting sender...");
//        this.startSender();
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
