package eu.telecomnancy.amio_2015_hcmm.model;

import java.util.List;

/**
 * Created by Mathieu on 23/01/2015.
 */
public class Motes {
    private int motesNb;
    private List<Sender> sender;
    private List<Sink> sink;

    public int getMotesNb() {
        return motesNb;
    }

    public void setMotesNb(int motesNb) {
        this.motesNb = motesNb;
    }

    public List<Sender> getSender() {
        return sender;
    }

    public void setSender(List<Sender> sender) {
        this.sender = sender;
    }

    public List<Sink> getSink() {
        return sink;
    }

    public void setSink(List<Sink> sink) {
        this.sink = sink;
    }
}
