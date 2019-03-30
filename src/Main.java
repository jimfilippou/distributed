import Helpers.BusReader;
import Models.Bus;

import java.util.*;
import java.io.*;
import java.lang.*;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        List<Bus> buses = BusReader.fetchBuses();

        // Summon brokers
        for (int port = 8080; port < 8080 + 5; port++) {
            new Broker(port).start();
        }

        new Publisher().start();

    }
}
