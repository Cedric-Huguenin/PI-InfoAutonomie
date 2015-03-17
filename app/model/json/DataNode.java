package model.json;

import java.util.List;

/**
 * Node storing data collected from a sensor to adapt it to the JSON parser.
 * Created by Mathieu on 23/01/2015.
 */
public class DataNode {
    /**
     * The list containing the data to be converted in JSON or from JSON.
     */
    public List<Data> data;

    /**
     * Returns the involved data.
     * @return the raw data from the sensor.
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * Changes the stored data.
     * @param data the new data.
     */
    public void setData(List<Data> data) {
        this.data = data;
    }
}
