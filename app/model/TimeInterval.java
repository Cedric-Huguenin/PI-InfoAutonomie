package model;

/**
 * Created by Mathieu on 31/01/2015.
 */
public class TimeInterval {
    public long timestampStart;
    public long timestampEnd;

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
}
