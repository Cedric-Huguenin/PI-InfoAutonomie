package model.json;

import java.util.List;

/**
 * Bean for the storage of information about the motes.
 * Created by Mathieu on 23/01/2015.
 */
public class Motes {
    /**
     * Number of motes in the network.
     */
    private int motesNb;
    /**
     * List of the motes acting as senders.
     */
    private List<Sender> sender;
    /**
     * List of the motes acting as sinks.
     */
    private List<Sink> sink;

    /**
     * Get the number of motes.
     * @return the motes count.
     */
    public int getMotesNb() {
        return motesNb;
    }

    /**
     * Changes the motes number. TODO add of the size of the two lists ?
     * @param motesNb the new count.
     */
    public void setMotesNb(int motesNb) {
        this.motesNb = motesNb;
    }

    /**
     * Returns the list of the motes acting as senders.
     * @return the list of the motes acting as senders.
     */
    public List<Sender> Returnsender() {
        return sender;
    }

    /**
     * Changes the list of the motes acting as senders.
     * @param sender the new list.
     */
    public void setSender(List<Sender> sender) {
        this.sender = sender;
    }

    /**
     * Returns the list of motes acting as sinks.
     * @return the list of sinks.
     */
    public List<Sink> Returnsink() {
        return sink;
    }

    /**
     * Changes the list of motes acting as sinks.
     * @param sink the new list.0
     */
    public void setSink(List<Sink> sink) {
        this.sink = sink;
    }
}
