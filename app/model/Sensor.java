package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * TODO document
 * Created by Mathieu on 31/01/2015.
 */

@Entity
public class Sensor extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;
    public String name;
    public String address;
    public SensorType type;
    public String location;
    public String description;

    public static Sensor create(Sensor sensor) {
        sensor.save();
        return sensor;
    }

    public static Model.Finder<String,Sensor> find = new Model.Finder<>(String.class, Sensor.class);

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

    @Override
    public String toString() {
        return "Sensor{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
