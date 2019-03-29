package Helpers;

import Models.Bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BusReader {
    public static List<Bus> fetchBuses() throws FileNotFoundException {
         Scanner input = new Scanner(new File("/Users/jimfilippou/Projects/distributed/src/Data/busLines.txt"));
        // Scanner input = new Scanner(new File("/home/jimfilippou/IdeaProjects/distributed/src/Data/busLines.txt"));
        input.useDelimiter("-\n");
        List<Models.Bus> buses = new ArrayList<>();
        while (input.hasNextLine()) {
            String data = input.nextLine();
            String lineNumber = data.split(",")[0];
            String busLineID = data.split(",")[1];
            String lineName = data.split(",")[2];
            Models.Bus bus = new Models.Bus(lineNumber, busLineID, lineName);
            buses.add(bus);
        }
        return buses;
    }
}
