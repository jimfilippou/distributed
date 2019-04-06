import Models.Publisher;
import Models.Stigma;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class PublisherEntity {

    private Publisher publisher;
    private Queue<HashMap<Integer, Stigma>> data = new LinkedList<>();

    public PublisherEntity(Publisher publisher) {
        this.publisher = publisher;
    }

}
