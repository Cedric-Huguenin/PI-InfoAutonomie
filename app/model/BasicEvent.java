package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;
import utils.TimestampUtils;

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
     * The ID identifying the BasicEvent.
     */
    @Id
    public String id;

    public String name;

    /**
     * The sensor providing the data used to recognize this event.
     */
    @ManyToOne
    public Sensor sensor;

    /**
     * Icon name to display the correct icon
     */
    public String icon;

    public String color;

    public DetectionType detectionType;
    /**
     * The threshold which triggers the event.
     */
    public double simpleThreshold;
    /**
     * The minimum value of the measure.
     */
    public double minValue;
    /**
     * The maximum value of the measure.
     */
    public double maxValue;
    /**
     * The delta of the 2 last values.
     */
    public double delta;

    public static Model.Finder<String, BasicEvent> find = new Model.Finder<>(String.class, BasicEvent.class);

    /**
     * Creates a new BasicEvent and saves it in the database.
     *
     * @param basicEvent      the BasicEvent that must be initialized.
     * @param sensor          the identifier of the sensor used.
     * @return the BasicEvent given updated and saved.
     */
    public static BasicEvent create(BasicEvent basicEvent, String sensor) {
        basicEvent.setSensor(Sensor.find.byId(sensor));
        basicEvent.setId(basicEvent.getName().replaceAll(" ", "_").toLowerCase());
        basicEvent.save();
        return basicEvent;
    }

    /**
     * Returns the list of all the BasicEvent created.
     *
     * @return the list of all the BasicEvent created.
     */
    public static List<BasicEvent> all() {
        return find.all();
    }

    /**
     * Returns the BasicEvent with the given id.
     *
     * @return the list of all the BasicEvent created.
     */
    public static BasicEvent byId(String id) {
        return find.byId(id);
    }

    public void check() {
        // TODO: select the correct range to retrieve data
//            System.out.println("Checking " + this);
        model.Data old = null;
        List<model.Data> dataList;
        if(BasicEventOccurrence.find.where().eq("basic_event_id", getId()).findList().size() == 0) { // if it's the first computation for this basic event, use all available data
            dataList = model.Data.find.where().eq("mote", this.getSensor().getId()).findList();
        } else { // else only use a short time windows period
            long now = System.currentTimeMillis()/1000; // current second timestamp
            dataList = model.Data.find.where().eq("mote", this.getSensor().getId()).between("timestamp",now-3600,now).findList();
        }
        switch (getDetectionType()) {
            case DELTA:
                System.out.println(getName() + " " + dataList.size());
                for (model.Data data : dataList) {
                    //System.out.println(data);
                    if (old != null) {
                        //System.out.println(getName() + " " + Math.abs(data.getValue() - old.getValue()));
                        if (Math.abs(data.getValue() - old.getValue()) > getDelta()) {
                            BasicEventOccurrence occurrence = new BasicEventOccurrence(this, TimestampUtils.formatToString(data.getTimestamp(), "dd-MM-yyyy HH:mm:ss"),
                                    data.getTimestamp(), old.getValue(), data.getValue());
                            try {
                                if (BasicEventOccurrence.find.where().eq("timestamp", occurrence.getTimestamp()).eq("basic_event_id", occurrence.getBasicEvent().getId()).findUnique() == null) {
                                    occurrence.save();
                                    //System.out.println(occurrence);
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e);
                            }
                        }
                    }
                    old = data;
                }
                break;
            case MIN_MAX_VALUES:
                System.out.println(getName() + " " + dataList.size());
                for (model.Data data : dataList) {
//                    System.out.println("Value : " + data.getValue() + " min : " + detectionMethod.getMinValue() + " max : " + detectionMethod.getMaxValue());
                    if (old != null && old.getValue() != data.getValue() && (data.getValue() <= getMinValue() || data.getValue() >= getMaxValue())) {
                        BasicEventOccurrence occurrence = new BasicEventOccurrence(this, TimestampUtils.formatToString(data.getTimestamp(), "dd-MM-yyyy HH:mm:ss"),
                                data.getTimestamp(), old == null ? -1 : old.getValue(), data.getValue());
                        try {
                            if (BasicEventOccurrence.find.where().eq("timestamp", occurrence.getTimestamp()).eq("basic_event_id", occurrence.getBasicEvent().getId()).findUnique() == null) {
                                occurrence.save();
                                //System.out.println(occurrence);
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e);

                        }
                    }
                    old = data;
                }
                break;
            case SIMPLE_THRESHOLD:
                System.out.println(getName() + " " + dataList.size());
                for (model.Data data : dataList) {
                    if (old != null && old.getValue() != data.getValue() && (data.getValue() > getSimpleThreshold())) {
                        BasicEventOccurrence occurrence = new BasicEventOccurrence(this, TimestampUtils.formatToString(data.getTimestamp(), "dd-MM-yyyy HH:mm:ss"),
                                data.getTimestamp(), old == null ? -1 : old.getValue(), data.getValue());
                        try {
                            if (BasicEventOccurrence.find.where().eq("timestamp", occurrence.getTimestamp()).eq("basic_event_id", occurrence.getBasicEvent().getId()).findUnique() == null) {
                                occurrence.save();
                                //System.out.println(occurrence);
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e);
                        }
                    }
                    old = data;
                    String test = "blabla";
                    test.split("", 0);
                }
                break;
            // TODO: add other detection method
            default:
                break;
        }
    }

    public String getValueAsString() {
        String valueAsString;

        switch (detectionType) {
            case DELTA:
                valueAsString = "delta : " + delta;
                break;
            case MIN_MAX_VALUES:
                valueAsString = "min/max : " + minValue + "/" + maxValue;
                break;
            case SIMPLE_THRESHOLD:
                valueAsString = "seuil : " + simpleThreshold;
                break;
            default:
                valueAsString = "divers : pas de valeur";
                break;
        }

        return valueAsString;
    }

    /**
     * Returns the characteristics of the BasicEvent in JSON.
     *
     * @return a string in JSON representing the object.
     */
    @Override
    public String toString() {
        return "BasicEvent{" +
                "name='" + name + '\'' +
                ", sensor=" + sensor +
                ", icon='" + icon + '\'' +
                ", color='" + color + '\'' +
                ", detectionType=" + detectionType +
                ", simpleThreshold=" + simpleThreshold +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", delta=" + delta +
                ", id='" + id + '\'' +
                '}';
    }

    /**
     * Returns the ID of the BasicEvent.
     *
     * @return the ID of the BasicEvent.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the BasicEvent.
     *
     * @param id the new ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the sensor used to detect the event.
     *
     * @return the sensor used to detect the event.
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Sets a new sensor for the BasicEvent.
     *
     * @param sensor the new sensor.
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public DetectionType getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(DetectionType detectionType) {
        this.detectionType = detectionType;
    }

    public double getSimpleThreshold() {
        return simpleThreshold;
    }

    public void setSimpleThreshold(double simpleThreshold) {
        this.simpleThreshold = simpleThreshold;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}
