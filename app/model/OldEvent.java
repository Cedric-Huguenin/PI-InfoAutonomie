package model;

/**
 * Created by Ced on 26/01/2015.
 */
public class OldEvent {

    public long timestamp;
    public double value;
    public String description;
    public String verboseDate;

    public OldEvent(long timestamp, String description, String verboseDate, double value) {
        this.timestamp = timestamp;
        this.description = description;
        this.verboseDate = verboseDate;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVerboseDate() {
        return verboseDate;
    }

    public void setVerboseDate(String verboseDate) {
        this.verboseDate = verboseDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
