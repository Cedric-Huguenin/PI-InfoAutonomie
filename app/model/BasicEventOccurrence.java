package model;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by Ced on 09/02/2015.
 */
@Entity
public class BasicEventOccurrence extends Model implements Comparable<BasicEventOccurrence> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    @ManyToOne
    public BasicEvent basicEvent;
    public long timestamp;
    public String date;
    public double fromValue, toValue;

    public BasicEventOccurrence(BasicEvent basicEvent, String date, long timestamp, double fromValue, double toValue) {
        this.basicEvent = basicEvent;
        this.date = date;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.timestamp = timestamp;
    }

    public static BasicEventOccurrence create(BasicEventOccurrence basicEventOccurrence) {
        basicEventOccurrence.save();
        return basicEventOccurrence;
    }

    public static Model.Finder<String,BasicEventOccurrence> find = new Model.Finder<>(String.class, BasicEventOccurrence.class);

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getFromValue() {
        return fromValue;
    }

    public void setFromValue(double fromValue) {
        this.fromValue = fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public void setToValue(double toValue) {
        this.toValue = toValue;
    }

    public BasicEvent getBasicEvent() {
        return basicEvent;
    }

    public void setBasicEvent(BasicEvent basicEvent) {
        this.basicEvent = basicEvent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(BasicEventOccurrence o) {
        return (int) (timestamp - o.getTimestamp());
    }
}
