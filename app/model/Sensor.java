package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Mathieu on 31/01/2015.
 */

@Entity
public class Sensor extends Model {

    @Id
    public String name;
    public SensorType type;
    public String location;
    public String description;

    public static Model.Finder<String,Sensor> find = new Model.Finder(String.class, Sensor.class);

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

    @Override
    public String toString() {
        return "Sensor{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
