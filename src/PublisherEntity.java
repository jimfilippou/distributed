import Helpers.BusReader;
import Models.Publisher;
import Models.Stigma;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

class PublisherEntity {

    private Publisher publisher;
    private HashMap<Integer, Queue<Stigma>> data = new HashMap<>();

    PublisherEntity(Publisher publisher) {
        this.publisher = publisher;
    }

    void start() {
        try {
            for (int busID : this.publisher.getTopics()) {
                for (Stigma stigma : BusReader.readBusPositions(busID)) {
                    TimeUnit.SECONDS.sleep(5);
                    if (this.data.get(busID) == null) {
                        this.data.put(busID, new LinkedList<>());
                        this.data.get(busID).add(stigma);
                    } else {
                        this.data.get(busID).add(stigma);
                    }
                    System.out.println(this.publisher.toString() + " Got data from sensor -> " + busID + ": " + stigma);
                }
            }
            System.out.println(data);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    void addTopic(int busLineID) {
        this.publisher.addTopic(busLineID);
    }

}
