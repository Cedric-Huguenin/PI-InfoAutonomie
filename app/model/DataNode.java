package model;

import java.util.List;

/**
 * Created by Mathieu on 23/01/2015.
 */
public class DataNode {
    private List<DataNode> data;

    public List<DataNode> getData() {
        return data;
    }

    public void setData(List<DataNode> data) {
        this.data = data;
    }
}
