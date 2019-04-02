import Models.Stigma;
import Helpers.BrokerProvider;
import com.sun.deploy.ref.Helpers;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Broker implements Runnable {

    private int _port;
    private String _ip;
    private Thread _t;

    public Broker(String ip, int port) {
        this._ip = ip;
        this._port = port;
    }

    private void _startSender() {
        (new Thread(() -> {
            try {

                for (String broker : BrokerProvider.fetchBrokers()) {
                    Socket s = new Socket(broker.split(":")[0], Integer.parseInt(broker.split(":")[1]));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    out.write(broker);
                    out.newLine();
                    out.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        })).start();
    }

    private void _startServer() {
        (new Thread(() -> {
            ServerSocket providerSocket = null;
            try {
                InetAddress addr = InetAddress.getByName("192.168.1.19");
                providerSocket = new ServerSocket(this._port, 50, addr);
                System.out.println("Broker started at:" + addr + ":" + this._port);
                Socket s = providerSocket.accept();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(s.getInputStream()));
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
        System.out.println("Starting sender...");
        this._startSender();
    }

    public void start() {
        String _thread_name = "THREAD" + this._port;
        Thread thread = this._t;
        if (thread == null) {
            thread = new Thread(this, _thread_name);
            thread.start();
        }
    }
}
