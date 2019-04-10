package Models;

import Helpers.Hash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Publisher implements Serializable {

    private static final long serialVersionUID = 578515438738407941L;
    private String ip;
    private int port;
    private List<Broker> brokers = new ArrayList<>();

    public List<Integer> getTopics() {
        return topics;
    }

    private List<Integer> topics = new ArrayList<>();

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public List<Broker> getBrokers() {
        return brokers;
    }

    public Publisher(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String hashThis(String toHash) {
        return Hash.hashWithMD5(toHash);
    }

    public void addTopic(int topic) {
        this.topics.add(topic);
    }

    @Override
    public String toString() {
        return "Publisher " + this.getIP() + ":" + this.getPort();
    }

}
