package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.json.Data;
import model.json.DataNode;
import play.db.ebean.Model;
import utils.TimestampUtils;

import javax.persistence.*;
import java.io.IOException;
import java.net.URL;
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
     * The method and criteria chosen to detect the event.
     */
    @ManyToOne
    public Detection detectionMethod;

    /**
     * Icon name to display the correct icon
     */
    public String icon;

    public static Model.Finder<String,BasicEvent> find = new Model.Finder<>(String.class, BasicEvent.class);

    /**
     * Creates a new BasicEvent and saves it in the database.
     * @param basicEvent the BasicEvent that must be initialized.
     * @param detectionMethod the method of detection of the event.
     * @param sensor the identifier of the sensor used.
     * @return the BasicEvent given updated and saved.
     */
    public static BasicEvent create(BasicEvent basicEvent, String detectionMethod, String sensor) {
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
     * Returns the BasicEvent with the given id.
     * @return the list of all the BasicEvent created.
     */
    public static BasicEvent byId(String id) {
        return find.byId(id);
    }

    public void check() {
        DataNode rawDataNode = null;
        String label = "light1";
        switch(sensor.getType()) {
            case LIGHT:
                label = "light1";
                break;
            case TEMP:
                label = "temperature";
                break;
        }
        try {
            URL url = new URL("http://iotlab.telecomnancy.eu/rest/data/1/"+ label +"/24");
            ObjectMapper mapper = new ObjectMapper();
            rawDataNode = mapper.readValue(url, DataNode.class);

            Data old = null;
            switch(detectionMethod.getDetectionType()) {
                case DELTA:
                    for (Data data : rawDataNode.getData()) {
                        if(old != null) {
                            if(Math.abs(data.getValue() - old.getValue()) > detectionMethod.getDelta()) {
                                BasicEventOccurrence occurrence = new BasicEventOccurrence(this, TimestampUtils.formatToString(data.getTimestamp(), "dd-MM-yyyy HH:mm:SS"),
                                        data.getTimestamp(), old.getValue(), data.getValue());
                                try {
                                    if(BasicEventOccurrence.find.where().eq("timestamp", occurrence.getTimestamp()).eq("basic_event_id", occurrence.getBasicEvent().getId()).findUnique() == null) {
                                        occurrence.save();
                                        System.out.println(occurrence);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error: " + e);
                                }
                            }
                        }
                        old = data;
                    }
                    break;
                case SIMPLE_THRESHOLD:
                    for(Data data : rawDataNode.getData()) {
                        if(data.getValue() > detectionMethod.getSimpleThreshold()) {
                            BasicEventOccurrence occurrence = new BasicEventOccurrence(this, TimestampUtils.formatToString(data.getTimestamp(), "dd-MM-yyyy HH:mm:SS"),
                                    data.getTimestamp(), old == null ? -1 : old.getValue(), data.getValue());
                            try {
                                if(BasicEventOccurrence.find.where().eq("timestamp", occurrence.getTimestamp()).eq("basic_event_id", occurrence.getBasicEvent().getId()).findUnique() == null) {
                                    occurrence.save();
                                    System.out.println(occurrence);
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e);
                            }
                        }
                        old = data;
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the characteristics of the BasicEvent in JSON.
     * @return a string in JSON representing the object.
     */
    @Override
    public String toString() {
        return "BasicEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sensor=" + sensor +
                ", detectionMethod=" + detectionMethod +
                ", icon='" + icon + '\'' +
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
}
