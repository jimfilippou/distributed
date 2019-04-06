package Helpers;

import Models.Bus;
import Models.Stigma;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.lang.String;

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
            Bus bus = new Bus(lineNumber, busLineID, lineName);
            buses.add(bus);
        }
        return buses;
    }

    public static ArrayList<Stigma> readBusPositions(int busID) throws FileNotFoundException {
        Scanner input = new Scanner(new File("/Users/jimfilippou/Projects/distributed/src/Data/busPositions.txt"));
        input.useDelimiter("-\n");
        ArrayList<Stigma> response = new ArrayList<>();
        while (input.hasNextLine()) {
            String[] data = input.nextLine().split(",");
            String busId = data[0];
            double x = Double.valueOf(data[3]);
            double y = Double.valueOf(data[4]);
            String timestamp = data[5];
            if (String.valueOf(busID).equals(busId)) {
                response.add(new Stigma(x, y, timestamp));
            }
        }
        return response;
    }
}
