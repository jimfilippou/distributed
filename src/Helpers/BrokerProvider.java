package Helpers;

import Models.Bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BrokerProvider {
    public static List<String> fetchBrokers() throws FileNotFoundException {
        Scanner input = new Scanner(new File("/Users/jimfilippou/Projects/distributed/src/Data/brokers.txt"));
        input.useDelimiter("-\n");
        List brokers = new ArrayList<>();
        while (input.hasNextLine()) {
            String data = input.nextLine();
            brokers.add(data);
        }
        return brokers;
    }
}