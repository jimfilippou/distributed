package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Consumer implements Serializable {

    private static final long serialVersionUID = 578515438738407941L;
    private String ip;
    private int port;
    private List<Integer> interests = new ArrayList<>();

    public Consumer(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public List<Integer> getInterests() {
        return this.interests;
    }

    public void addInterest(Integer topic) {
        this.interests.add(topic);
    }

    @Override
    public String toString() {
        return "Publisher " + this.getIP() + ":" + this.getPort();
    }

}
