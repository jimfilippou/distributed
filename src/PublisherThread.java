import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class PublisherThread implements Runnable {

    private Thread _t;

    @Override
    public void run() {
        try {
            Socket socket = new Socket("0.0.0.0",9090);
            try {
                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream()); //Error Line!
                try {
                    Object object = objectInput.readObject();
//                    titleList =  (ArrayList<String>) object;
                    System.out.println(object.hashCode());
                } catch (ClassNotFoundException e) {
                    System.out.println("The title list has not come from the server");
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println("The socket for reading the object has problem");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Thread thread = this._t;
        if (thread == null) {
            thread = new Thread(this);
            System.out.println("PublisherThread started");
            thread.start();
        }
    }

}
