package Models;

import Helpers.Hash;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Broker {

    private String ip;
    private String hash;
    private int port;
    private HashMap<String, Broker> hashes;
    private List<Publisher> registeredPublishers;
    private List<Subscriber> registeredSubscribers;
    private HashMap<Integer, Queue<Stigma>> data = new HashMap<>();

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getHash() {
        return hash;
    }

    public HashMap<String, Broker> getHashes() {
        return hashes;
    }

    public List<Publisher> getRegisteredPublishers() {
        return registeredPublishers;
    }

    public List<Subscriber> getRegisteredSubscribers() {
        return registeredSubscribers;
    }

    public HashMap<Integer, Queue<Stigma>> getData() {
        return data;
    }

    public Broker(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.hash = this.hashThis(ip.concat(String.valueOf(port)));
    }

    private String hashThis(String toHash) {
        return Hash.hashWithMD5(toHash);
    }

    public void receiveData(Integer topic, Stigma value){
        if (this.data.get(topic) == null) {
            this.data.put(topic, new LinkedList<>());
            this.data.get(topic).add(value);
        } else {
            this.data.get(topic).add(value);
        }
    }

    @Override
    public String toString() {
        return "Broker " + this.getIP() + ":" + this.getPort();
    }

}
