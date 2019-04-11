package Helpers;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static String hashWithMD5(String toHash) {
        String resp = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(toHash.getBytes());
            byte[] digest = messageDigest.digest();
            resp = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public static void main(String[] args) {
        String broker = hashWithMD5("192.168.1.4:8080");
        String busID = hashWithMD5(String.valueOf(820));
        System.out.println(broker);
        System.out.println(busID);
        if (broker.compareTo(busID) > 0) {
            System.out.println("Broker bigger");

        }else{
            System.out.println("smlaller");
        }
    }
}
