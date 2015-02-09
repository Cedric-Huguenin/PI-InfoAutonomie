package model;

import java.util.GregorianCalendar;

/**
 * Created by Ced on 09/02/2015.
 */
public class BasicEventOccurrence {

    public BasicEvent basicEvent;
    public GregorianCalendar date;
    public double fromValue, toValue;

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
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
}
