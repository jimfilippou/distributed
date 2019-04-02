import Models.Stigma;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Broker implements Runnable {

    private int _port;
    private Thread _t;

    public Broker(int port) {
        this._port = port;
    }

    private void _startSender() {
        (new Thread(() -> {
            try {
                Socket s = new Socket("192.168.1.19", 8080);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

                while (true) {
                    out.write("Hello World!");
                    out.newLine();
                    out.flush();

                    Thread.sleep(200);
                }

            } catch (IOException | InterruptedException e) {
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
                System.out.println("Broker started at:" + addr +  ":" + this._port);
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
