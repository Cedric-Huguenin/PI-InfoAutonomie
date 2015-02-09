package model;

/**
 * Created by Ced on 09/02/2015.
 */
public class BasicEventOccurrence {

    public BasicEvent basicEvent;
    public String date;
    public double fromValue, toValue;

    public BasicEventOccurrence(BasicEvent basicEvent, String date, double fromValue, double toValue) {
        this.basicEvent = basicEvent;
        this.date = date;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

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
}
