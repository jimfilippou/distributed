import java.net.ServerSocket;
import java.net.Socket;

public class BrokerImpl implements Broker, Runnable {
    @Override
    public void calculateKeys() {

    }

    @Override
    public Publisher acceptConnection(Publisher publisher) {
        return null;
    }

    @Override
    public Subscriber acceptConnection(Subscriber subscriber) {
        return null;
    }

    @Override
    public void notifyPublisher(String message) {

    }

    @Override
    public void pull(Topic topic) {

    }

    @Override
    public void init(int x) {
        ServerSocket socket = null;
        Socket connection = null;

    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void updateNodes() {

    }

    @Override
    public void run() {

    }
}
