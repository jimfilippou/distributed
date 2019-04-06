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
            Models.Bus bus = new Models.Bus(lineNumber, busLineID, lineName);
            buses.add(bus);
        }
        return buses;
    }

    public static HashMap<Integer, Stigma> readBusPositions(int busID) throws FileNotFoundException {
        Scanner input = new Scanner(new File("Users/jimfilippou/Projects/distributed/src/Data/busPositions.txtz"));
        input.useDelimiter("-\n");
        HashMap<Integer, Stigma> response = new HashMap<>();
        while (input.hasNextLine()) {
            String data = input.nextLine();
            String busId = data.split(",")[0];
            String x = data.split(",")[3];
            String y = data.split(",")[4];
            String timestamp = data.split(",")[5];
            if (String.valueOf(busID).equals(busId)) {
                Stigma stigma = new Stigma(Double.valueOf(x), Double.valueOf(y), timestamp);
                response.put(Integer.parseInt(busId), stigma);
            }
        }
        return response;
    }
}
