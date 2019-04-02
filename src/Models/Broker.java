package Models;

import Helpers.BrokerProvider;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Broker {

    private String _ip;
    private String _hash;
    private int _port;
    private HashMap<String, Broker> _hashes = new HashMap<String, Broker>();

    public String getIP() {
        return _ip;
    }

    public void setIP(String _ip) {
        this._ip = _ip;
    }

    public int getPort() {
        return _port;
    }

    public void setPort(int _port) {
        this._port = _port;
    }

    public String getHash() {
        return _hash;
    }

    public void setHash(String _hash) {
        this._hash = _hash;
    }

    public HashMap<String, Broker> getHashes() {
        return _hashes;
    }

    public void setHashes(HashMap<String, Broker> _hashes) {
        this._hashes = _hashes;
    }


    public Broker(String ip, int port) {
        this._ip = ip;
        this._port = port;
        this._hash = this._hashThis(ip.concat(String.valueOf(port)));
    }

    private String _hashThis(String toHash) {
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
}
