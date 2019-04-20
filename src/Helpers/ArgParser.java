package Helpers;

import Models.Publisher;

import java.util.ArrayList;
import java.util.List;

public class ArgParser {
    public static List<Publisher> fetchPublishersFromCommandLine(String[] args) throws IllegalArgumentException {
        List<Publisher> publishers = new ArrayList<>();
        int position = -1;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                position = i;
            }
        }
        if (position == -1)
            throw new IllegalArgumentException();
        try {
            while (args[++position] != null) {
                String[] data = args[position].split(":");
                Publisher publisher = new Publisher(data[0], Integer.parseInt(data[1]));
                publishers.add(publisher);
            }
            // This line never executes, however compiler warned me.
            throw new IndexOutOfBoundsException();
        } catch (IndexOutOfBoundsException err) {
            return publishers;
        }
    }
}
