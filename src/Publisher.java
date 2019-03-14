public interface Publisher extends Node{
    void getBrokerList();
    Broker hashTopic(Topic topic);
    void push(Topic topic, Value value);
    void notifyFailure(Broker broker);
}
