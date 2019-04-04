package Helpers;


import Models.Broker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BrokerProvider {

    public static List<Broker> fetchBrokers() {
        Scanner input = null;
        try {
            input = new Scanner(new File("/home/p3160253/IdeaProjects/distributed/src/Data/brokers.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        input.useDelimiter("-\n");
        List<Broker> brokers = new ArrayList<>();
        while (input.hasNextLine()) {
            String data = input.nextLine();
            String ip = data.split(":")[0];
            int port = Integer.parseInt(data.split(":")[1]);
            Broker broker = new Broker(ip, port);
            brokers.add(broker);
        }
        return brokers;
    }
}