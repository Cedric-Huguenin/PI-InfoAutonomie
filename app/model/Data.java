package eu.telecomnancy.amio_2015_hcmm.model;

/**
 * Created by Mathieu on 23/01/2015.
 */
public class Data {
    private int timestamp;
    private String label;
    private double value;
    private String mote;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getMote() {
        return mote;
    }

    public void setMote(String mote) {
        this.mote = mote;
    }
}
