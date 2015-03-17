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

    public int beginHour;
    public int beginMinutes;
    public int endHour;
    public int endMinutes;

    public static TimeInterval create(TimeInterval timeInterval) {
        timeInterval.save();
        return timeInterval;
    }

    public static Model.Finder<String, TimeInterval> find = new Model.Finder<>(String.class, TimeInterval.class);

    /**
     * Compute a TimeInterval for the current day
     * @return a new TimeInterval corresponding to the current day
     */
    public long[] getActualTimeInterval() {
        long[] res = new long[2];
        res[0] = getTimestampStart();
        res[1] = getTimestampEnd();
        return res;
    }

    public long getTimestampStart() {
        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(System.currentTimeMillis());
        start.set(Calendar.HOUR_OF_DAY, beginHour);
        start.set(Calendar.MINUTE, beginMinutes);
        start.set(Calendar.SECOND, 0);

        if(start.getTimeInMillis() > System.currentTimeMillis()) { // it's 1:00 and event begin at 6 for example
            return start.getTimeInMillis()/1000 - 24*3600; // subtract on day in second
        } else {
            return start.getTimeInMillis()/1000;
        }
    }


    public long getTimestampEnd() {
        GregorianCalendar end = new GregorianCalendar();
        end.setTimeInMillis(System.currentTimeMillis());
        end.set(Calendar.HOUR_OF_DAY, endHour);
        end.set(Calendar.MINUTE, endMinutes);
        end.set(Calendar.SECOND, 0);

        if(end.getTimeInMillis() > System.currentTimeMillis()) { // it's 1:00 and event begin at 6 for example
            return end.getTimeInMillis()/1000 - 24*3600; // subtract on day in second
        } else {
            return end.getTimeInMillis()/1000;
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getBeginMinutes() {
        return beginMinutes;
    }

    public void setBeginMinutes(int beginMinutes) {
        this.beginMinutes = beginMinutes;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
    }

    @Override
    public String toString() {
        return "TimeInterval{" +
                "id='" + id + '\'' +
                ", beginHour=" + beginHour +
                ", beginMinutes=" + beginMinutes +
                ", endHour=" + endHour +
                ", endMinutes=" + endMinutes +
                '}';
    }
}
