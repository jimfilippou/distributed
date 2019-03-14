import java.util.List;

public interface Broker extends Node {
    List<Subscriber> registeredSubscribers = null;
    List<Publisher> registeredPublishers = null;
    void calculateKeys();
    Publisher acceptConnection(Publisher publisher);
    Subscriber acceptConnection(Subscriber subscriber);
    void notifyPublisher(String message);
    void pull(Topic topic);
}
