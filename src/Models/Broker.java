package Models;

import Helpers.Hash;

import java.io.Serializable;
import java.util.*;

public class Broker implements Serializable {

    private static final long serialVersionUID = 578515438738407941L;
    private String ip;
    private String hash;
    private int port;
    private List<Publisher> registeredPublishers = new ArrayList<>();
    private List<Consumer> registeredConsumers = new ArrayList<>();
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

    public List<Publisher> getRegisteredPublishers() {
        return registeredPublishers;
    }

    public List<Consumer> getRegisteredConsumers() {
        return registeredConsumers;
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

    public void addPublisher(Publisher publisher) {
        this.registeredPublishers.add(publisher);
    }

    @Override
    public String toString() {
        return "Broker " + this.getIP() + ":" + this.getPort();
    }

}
