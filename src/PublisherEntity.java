import Helpers.BusReader;
import Models.Publisher;
import Models.Stigma;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

class PublisherEntity {

    private Publisher publisher;


    PublisherEntity(Publisher publisher) {
        this.publisher = publisher;
    }

    void start() {
        try {
            for (int busID : this.publisher.getTopics()) {
                for (Stigma stigma : BusReader.readBusPositions(busID)) {
                    TimeUnit.SECONDS.sleep(3);
                    push(busID, stigma);
                    System.out.println(this.publisher.toString() + " Got data from sensor -> " + busID + ": " + stigma);
                }
            }
//            System.out.println(data);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void push(int topic, Stigma value) {

    }

    PublisherEntity addTopic(int busLineID) {
        this.publisher.addTopic(busLineID);
        return this;
    }

}
