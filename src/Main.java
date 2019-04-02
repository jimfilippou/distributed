import Helpers.BusReader;
import Models.Bus;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.lang.*;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

//        int brokers = 5;
//
//        List<Bus> buses = BusReader.fetchBuses();
//
//        for (Bus bus: buses){
//            System.out.println(bus.busLineID);
//            try {
//                String stringToHash = bus.busLineID;
//                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
//                messageDigest.update(stringToHash.getBytes());
//                byte[] digiest = messageDigest.digest();
//                String hashedOutput = DatatypeConverter.printHexBinary(digiest);
//                System.out.println(hashedOutput);
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//        }

        // Summon brokers
//        for (int port = 8080; port < 8080 + brokers; port++) {
//            new Broker(port).start();
//        }
        new Broker(8080).start();
//
//        new Publisher().start();

    }
}
