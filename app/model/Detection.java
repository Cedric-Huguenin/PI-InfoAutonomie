package model;

/**
 * Created by Mathieu on 31/01/2015.
 */
public class Detection {
    public double simpleThreshold;
    public double minValue;
    public double maxValue;
    public double delta;

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
