package model;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by Ced on 03/03/2015.
 */

@Entity
public class EventOccurrence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    @ManyToOne
    public Event event;

    public long timestamp;
    public String date;

    public EventOccurrence(Event event, long timestamp, String date) {
        this.event = event;
        this.timestamp = timestamp;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
