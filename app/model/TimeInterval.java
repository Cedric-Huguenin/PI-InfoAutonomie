package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * TODO document
 * Created by Mathieu on 31/01/2015.
 */

@Entity
public class TimeInterval extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    public long timestampStart;
    public long timestampEnd;

    public static TimeInterval create(TimeInterval timeInterval) {
        timeInterval.save();
        return timeInterval;
    }

    public static Model.Finder<String,TimeInterval> find = new Model.Finder<>(String.class, TimeInterval.class);

    public long getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(long timestampStart) {
        this.timestampStart = timestampStart;
    }

    public long getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(long timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TimeInterval{" +
                "id='" + getId() + '\'' +
                ", timestampStart=" + getTimestampStart() +
                ", timestampEnd=" + getTimestampEnd() +
                '}';
    }
}
