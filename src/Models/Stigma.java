package Models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Stigma implements Serializable {

    private static final long serialVersionUID = 578515438738407941L;
    private double lat;
    private double lng;
    private Integer topic;
    private Timestamp timestamp;

    public Stigma(double lat, double lng, String timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.setTimestamp(timestamp);
    }

    private void setTimestamp(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MMM  d yyyy hh:mm:ss:SSSa", Locale.ENGLISH
            );
            Date parsedDate = dateFormat.parse(time);
            this.timestamp = new Timestamp(parsedDate.getTime());
        } catch (ParseException err) {
            err.printStackTrace();
        }
    }

    public void setTopic(Integer topic) {
        this.topic = topic;
    }

    public Integer getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "S{" + this.lat + ", " + this.lng + "}";
    }
}
