package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Describe a period during which an event can occur
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

    public static Model.Finder<String, TimeInterval> find = new Model.Finder<>(String.class, TimeInterval.class);

    public TimeInterval getActualTimeInterval() {
        TimeInterval result = new TimeInterval();

        // correct time but not the right day
        GregorianCalendar old = new GregorianCalendar();
        old.setTimeInMillis(this.getTimestampStart() * 1000);

        // the right day
        GregorianCalendar beginToday = new GregorianCalendar();
        beginToday.setTimeInMillis(System.currentTimeMillis());
        // and set the defined time
        beginToday.set(Calendar.HOUR_OF_DAY, old.get(Calendar.HOUR_OF_DAY));
        beginToday.set(Calendar.MINUTE, old.get(Calendar.MINUTE));
        beginToday.set(Calendar.SECOND, old.get(Calendar.SECOND));

        // adjust to current day or past day
        if (beginToday.getTimeInMillis() > System.currentTimeMillis()) { // if begin is in future
            // subtract one day
            beginToday.add(Calendar.DAY_OF_YEAR, -1);
        }


        old.setTimeInMillis(this.getTimestampEnd() * 1000);

        // the right day
        GregorianCalendar endToday = new GregorianCalendar();
        endToday.setTimeInMillis(System.currentTimeMillis());
        // and set the defined time
        endToday.set(Calendar.HOUR_OF_DAY, old.get(Calendar.HOUR_OF_DAY));
        endToday.set(Calendar.MINUTE, old.get(Calendar.MINUTE));
        endToday.set(Calendar.SECOND, old.get(Calendar.SECOND));

        // adjust to current day or past day
        if (endToday.getTimeInMillis() > System.currentTimeMillis()) { // if end is in future
            // subtract one day
            endToday.add(Calendar.DAY_OF_YEAR, -1);
        } else if (beginToday.getTimeInMillis() < System.currentTimeMillis() && endToday.getTimeInMillis() > System.currentTimeMillis()) {
            // current time is between begin and end hour
            endToday.setTimeInMillis(System.currentTimeMillis());
        }

        result.setTimestampStart(beginToday.getTimeInMillis());
        result.setTimestampEnd(endToday.getTimeInMillis());

        return result;
    }

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
