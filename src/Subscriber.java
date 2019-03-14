public interface Subscriber extends Node {
    void register(Broker broker, Topic topic);
    void disconnect(Broker broker, Topic topic);
    void visualizeData(Topic topic, Value value);
}
