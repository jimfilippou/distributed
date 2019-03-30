import Models.Stigma;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Broker implements Runnable {

    private int _port;
    private Thread _t;

    public Broker(int port) {
        this._port = port;
    }

    private void _startListening() {
        ServerSocket providerSocket = null;
        Socket connection = null;
        try {
            providerSocket = new ServerSocket(this._port);
            System.out.println("Broker started at: 127.0.0.1:" + this._port);
            while (true) {
                connection = providerSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());

                System.out.println((Stigma) in.readObject());

                in.close();
                out.close();
                connection.close();
            }
        } catch (IOException | ClassNotFoundException err) {
            err.printStackTrace();
        } finally {
            try {
                if (providerSocket != null) {
                    providerSocket.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        this._startListening();
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
