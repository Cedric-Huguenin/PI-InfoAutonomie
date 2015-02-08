package model;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * This class provides basic events that can directly be deducted from the sensors' raw data. It can be used as "bricks"
 * in more sophisticated Events (see Event class definition).
 * Note that it is persistent.
 * Created by Mathieu on 31/01/2015.
 */

@Entity
public class BasicEvent extends Model {

    /**
     * The ID identifying the Event.
     */
    @Id
    public String id;
    /**
     * The sensor providing the data used to recognize this event.
     */
    @ManyToOne
    public Sensor sensor;
    /**
     * The time interval which should be necessary to identify a whole new Event.
     */
    @ManyToOne
    public TimeInterval basicEventInterval;
    /**
     * The duration of the Event in seconds.
     */
    public long duration;
    /**
     * The method and criteria chosen to detect the event.
     */
    @ManyToOne
    public Detection detectionMethod;

    /**
     * The list of the different BasicEvent.
     */
    public static Model.Finder<String,BasicEvent> find = new Model.Finder<>(String.class, BasicEvent.class);

    /**
     * Creates a new BasicEvent and saves it in the database.
     * @param basicEvent the BasicEvent that must be initialized.
     * @param timeInterval the TimeInterval that must be set.
     * @param detectionMethod the method of detection of the event.
     * @param sensor the identifier of the sensor used.
     * @return the BasicEvent given updated and saved.
     */
    public static BasicEvent create(BasicEvent basicEvent, String timeInterval, String detectionMethod, String sensor) {
        basicEvent.setBasicEventInterval(TimeInterval.find.byId(timeInterval));
        basicEvent.setDetectionMethod(Detection.find.byId(detectionMethod));
        basicEvent.setSensor(Sensor.find.byId(sensor));
        basicEvent.save();
        return basicEvent;
    }

    /**
     * Returns the list of all the BasicEvent created.
     * @return the list of all the BasicEvent created.
     */
    public static List<BasicEvent> all() {
        return find.all();
    }

    /**
     * Returns the characteristics of the BasicEvent in JSON.
     * @return a string in JSON representing the object.
     */
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

    /**
     * Returns the ID of the BasicEvent.
     * @return the ID of the BasicEvent.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the BasicEvent.
     * @param id the new ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the sensor used to detect the event.
     * @return the sensor used to detect the event.
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Sets a new sensor for the BasicEvent.
     * @param sensor the new sensor.
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /**
     * Returns the TimeInterval defined for the BasicEvent.
     * @return the TimeInterval defined for the BasicEvent.
     */
    public TimeInterval getBasicEventInterval() {
        return basicEventInterval;
    }

    /**
     * Sets the TimeInterval for the BasicEvent.
     * @param basicEventInterval the new TimeInterval.
     */
    public void setBasicEventInterval(TimeInterval basicEventInterval) {
        this.basicEventInterval = basicEventInterval;
    }

    /**
     * Returns the duration of the BasicEvent.
     * @return the duration of the BasicEvent.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the BasicEvent.
     * @param duration the new duration of the BasicEvent.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Returns the method used to detect the Event.
     * @return the method used to detect the Event.
     */
    public Detection getDetectionMethod() {
        return detectionMethod;
    }

    /**
     * Sets the method used for the detection of the Event.
     * @param detectionMethod the new method.
     */
    public void setDetectionMethod(Detection detectionMethod) {
        this.detectionMethod = detectionMethod;
    }
}
