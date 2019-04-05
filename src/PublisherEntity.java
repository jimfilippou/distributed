import Models.Publisher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class PublisherEntity implements Runnable {

    private Thread t;
    private Publisher publisher;

    public PublisherEntity(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void run() {
    }

    public void start() {
        Thread thread = this.t;
        if (thread == null) {
            thread = new Thread(this);
            System.out.println("PublisherEntity started");
            thread.start();
        }
    }

}
