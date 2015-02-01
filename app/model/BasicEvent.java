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

    public static BasicEvent create(BasicEvent basicEvent, String timeInterval, String detectionMethod, String sensor) {
        basicEvent.setBasicEventInterval(TimeInterval.find.byId(timeInterval));
        basicEvent.setDetectionMethod(Detection.find.byId(detectionMethod));
        basicEvent.setSensor(Sensor.find.byId(sensor));
        basicEvent.save();
        return basicEvent;
    }

    public static Model.Finder<String,BasicEvent> find = new Model.Finder<>(String.class, BasicEvent.class);


    @Override
    public String toString() {
        return "BasicEvent{" +
                "id='" + getId() + '\'' +
                ", sensor=" + getSensor() +
                ", basicEventInterval=" + getBasicEventInterval() +
                ", duration=" + getDuration() +
                ", detectionMethod=" + getDetectionMethod() +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public TimeInterval getBasicEventInterval() {
        return basicEventInterval;
    }

    public void setBasicEventInterval(TimeInterval basicEventInterval) {
        this.basicEventInterval = basicEventInterval;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Detection getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(Detection detectionMethod) {
        this.detectionMethod = detectionMethod;
    }
}
