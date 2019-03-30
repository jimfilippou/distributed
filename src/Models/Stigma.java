package Models;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stigma {

    private double _lat;
    private double _lng;
    private Timestamp _timestamp;

    public Stigma(double lat, double lng, String timestamp) {
        this._lat = lat;
        this._lng = lng;
        this._setTimestamp(timestamp);
    }

    private void _setTimestamp(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MMM  d yyyy hh:mm:ss:SSSa"
            );
            Date parsedDate = dateFormat.parse(time);
            this._timestamp = new Timestamp(parsedDate.getTime());
        } catch (ParseException err) {
            err.printStackTrace();
        }
    }
}
