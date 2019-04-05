package Models;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public class Broker {

    private String ip;
    private String hash;
    private int port;
    private HashMap<String, Broker> hashes;
    private List<Publisher> registeredPublishers;
    private List<Subscriber> registeredSubscribers;

    public String getIP() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public HashMap<String, Broker> getHashes() {
        return hashes;
    }

    public void setHashes(HashMap<String, Broker> hashes) {
        this.hashes = hashes;
    }

    public List<Publisher> getRegisteredPublishers() {
        return registeredPublishers;
    }

    public void setRegisteredPublishers(List<Publisher> registeredPublishers) {
        this.registeredPublishers = registeredPublishers;
    }

    public List<Subscriber> getRegisteredSubscribers() {
        return registeredSubscribers;
    }

    public void setRegisteredSubscribers(List<Subscriber> registeredSubscribers) {
        this.registeredSubscribers = registeredSubscribers;
    }

    public Broker(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.hash = this.hashThis(ip.concat(String.valueOf(port)));
    }

    private String hashThis(String toHash) {
        String resp = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(toHash.getBytes());
            byte[] digest = messageDigest.digest();
            resp = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    public String toString() {
        return "Broker " + this.getIP() + ":" + this.getPort();
    }
}
