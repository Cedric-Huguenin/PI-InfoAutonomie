package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Describes the method of detection of an event and its criteria.
 * Created by Mathieu on 31/01/2015.
 */

@Entity
public class Detection extends Model {

    /**
     * The ID identifying the method.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;
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
    /**
     * The list of the different detection methods.
     */
    public static Model.Finder<String,Detection> find = new Model.Finder<>(String.class, Detection.class);

    /**
     * Saves the new Detection object.
     * @param detection
     * @return
     */
    public static Detection create(Detection detection) {
        detection.save();
        return detection;
    }

    /**
     * Returns the simple threshold triggering the detection.
     * @return the simple threshold triggering the detection.
     */
    public double getSimpleThreshold() {
        return simpleThreshold;
    }

    /**
     * Sets the new threshold triggering the detection.
     * @param simpleThreshold the new threshold.
     */
    public void setSimpleThreshold(double simpleThreshold) {
        this.simpleThreshold = simpleThreshold;
    }

    /**
     * Returns the minimum value that can be detected.
     * @return the minimum value that can be detected.
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * Sets the minimum value that can be detected.
     * @param minValue the new minimum value.
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * Returns the maximum value that can be detected.
     * @return the maximum value that can be detected.
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the maximum value that can be detected.
     * @param maxValue the new maximum value.
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Returns the delta between 2 values set.
     * @return the delta between 2 values set.
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Sets the delta between 2 values.
     * @param delta the new delta.
     */
    public void setDelta(double delta) {
        this.delta = delta;
    }

    /**
     * Returns the ID of the object.
     * @return the ID of the object.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the object.
     * @param id the new ID.
     */
    public void setId(String id) {
        setId(id);
    }

    public DetectionType getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(DetectionType detectionType) {
        this.detectionType = detectionType;
    }

    /**
     * Returns the description of this object in JSON.
     * @return the description of this object in JSON.
     */
    @Override
    public String toString() {
        return "Detection{" +
                "id='" + getId() + '\'' +
                ", detectionType=" + getDetectionType() +
                ", simpleThreshold=" + getSimpleThreshold() +
                ", minValue=" + getMinValue() +
                ", maxValue=" + getMaxValue() +
                ", delta=" + getDelta() +
                '}';
    }

    public String getDetectionTypeAsString() {
        String typeAsString;

        switch (detectionType) {
            case DELTA:
                typeAsString = "Delta";
                break;
            case MIN_MAX_VALUES:
                typeAsString = "Min/max";
                break;
            case SIMPLE_THRESHOLD:
                typeAsString = "Seuil";
                break;
            default:
                typeAsString = "Divers";
                break;
        }

        return typeAsString;
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
}
