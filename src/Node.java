import java.util.List;

public interface Node {
    List<Broker> brokers = null;
    void init(int x);
    void connect();
    void disconnect();
    void updateNodes();
    //    List<Broker> brokers;
}
