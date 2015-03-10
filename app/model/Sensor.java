package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * TODO document
 * Created by Mathieu on 31/01/2015.
 */

@Entity
public class Sensor extends Model {
    // {"id":"1","name":"Light Sensor","address":"153.111","type":"LIGHT","location":"Bureau TN","description":"Sur la commode"}
    @Id
    public String id;
    public String name;
    public String address;
    public SensorType type;
    public String location;
    public String description;
    public String unit;

    public static Sensor create(Sensor sensor) {
        sensor.save();
        return sensor;
    }

    public static Model.Finder<String,Sensor> find = new Model.Finder<>(String.class, Sensor.class);

    public static List<Sensor> all() {
        return find.all();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + getId() + '\'' +
                "name='" + getName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", type=" + getType() +
                ", location='" + getLocation() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", unit='" + getUnit() + '\'' +
                '}';
    }

    public String getTypeAsString() {
        String typeAsString;

        switch (type) {
            case DOOR:
                typeAsString = "Contact de porte";
                break;
            case LIGHT:
                typeAsString = "Luminosité";
                break;
            case TEMP:
                typeAsString = "Température";
                break;
            case HUMIDITY:
                typeAsString = "Humidité";
                break;
            case POWER:
                typeAsString = "Consommation électrique";
                break;
            case PRESENCE:
                typeAsString = "Présence";
                break;
            default:
                typeAsString = "Divers";
                break;
        }

        return typeAsString;
    }

    public List<BasicEvent> getAssociatedBasicEvents() {
        List<BasicEvent> basicEvents = BasicEvent.find.where().eq("SENSOR_ID", id).findList();
        return basicEvents;
    }
}
