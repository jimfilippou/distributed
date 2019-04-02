import Helpers.BrokerProvider;
import Models.Broker;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerThread implements Runnable {

    private Broker _broker;
    private Thread _t;

    public BrokerThread(Broker broker) {
        this._broker = broker;
    }

//    private void _startSender() {
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

    private void _startServer() {
        (new Thread(() -> {
            ServerSocket providerSocket = null;
            try {
                InetAddress addr = InetAddress.getByName("192.168.1.19");
                providerSocket = new ServerSocket(this._broker.getPort(), 50, addr);
                System.out.println("BrokerThread started at:" + addr + ":" + this._broker.getPort());
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
        this._startServer();
//        System.out.println("Starting sender...");
//        this._startSender();
    }

    public void start() {
        String _thread_name = "THREAD" + this._broker.getPort();
        Thread thread = this._t;
        if (thread == null) {
            thread = new Thread(this, _thread_name);
            thread.start();
        }
    }
}
