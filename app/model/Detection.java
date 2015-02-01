package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Mathieu on 31/01/2015.
 */

@Entity
public class Detection extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    public double simpleThreshold;
    public double minValue;
    public double maxValue;
    public double delta;

    public static Detection create(Detection detection) {
        detection.save();
        return detection;
    }

    public static Model.Finder<String,Detection> find = new Model.Finder<>(String.class, Detection.class);

    public double getSimpleThreshold() {
        return simpleThreshold;
    }

    public void setSimpleThreshold(double simpleThreshold) {
        setSimpleThreshold(simpleThreshold);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        setId(id);
    }

    @Override
    public String toString() {
        return "Detection{" +
                "id='" + getId() + '\'' +
                ", simpleThreshold=" + getSimpleThreshold() +
                ", minValue=" + getMinValue() +
                ", maxValue=" + getMaxValue() +
                ", delta=" + getDelta() +
                '}';
    }
}
