package model;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by Mathieu on 31/01/2015.
 */

@Entity
public class BasicEvent extends Model {

    @Id
    public String id;
    public Sensor sensor;
    @OneToOne
    public TimeInterval basicEventInterval;
    public long duration;
    @OneToOne
    public Detection detectionMethod;

    public static Model.Finder<String,BasicEvent> find = new Model.Finder(String.class, BasicEvent.class);

    @Override
    public String toString() {
        return "BasicEvent{" +
                "id='" + id + '\'' +
                ", sensor=" + sensor +
                ", basicEventInterval=" + basicEventInterval +
                ", duration=" + duration +
                ", detectionMethod=" + detectionMethod +
                '}';
    }
}
