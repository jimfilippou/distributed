package Models;

import Helpers.Hash;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Publisher {

    private String ip;
    private int port;
    private List<Integer> topics = new ArrayList<>();

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Publisher(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private String hashThis(String toHash) {
        return Hash.hashWithMD5(toHash);
    }

    public void addTopic(int topic) {
        this.topics.add(topic);
    }

}
