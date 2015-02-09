package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.BasicEvent;
import model.BasicEventOccurrence;
import model.json.Data;
import model.json.DataNode;
import play.mvc.Result;
import utils.TimestampUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static play.mvc.Results.ok;

/**
 * Controller to handle and display BasicEvent objects.
 * Created by Mathieu on 07/02/2015.
 */
public class BasicEventController {

    /**
     * Displays data about the basic events that occurred.
     * @return the basic events page result.
     */
    public static Result data() {
        BasicEvent basicEvent = BasicEvent.byId("Saut de luminosité");
        List<BasicEventOccurrence> basicEventList = new ArrayList<>();

        DataNode rawDataNode = null;
        String response = "";
        String date = "";
        long timestamp = 0;
        double value = 0.0;
//        List<String> basicEventList = new ArrayList<>();

        // first, parse the data obtained from the url and put it in a DataNode
        try {
            URL url = new URL("http://iotlab.telecomnancy.eu/rest/data/1/light1/24/153.111");
            ObjectMapper mapper = new ObjectMapper();
            rawDataNode = mapper.readValue(url, DataNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // then, extract the timestamp and the values from the data to compute the basic events
        if (rawDataNode != null) {
            List<Data> rawDataList = rawDataNode.getData();
            Collections.reverse(rawDataList);

            for (Data rawData : rawDataList) {
                double maxDifference = 30.0; // max 30 lux of difference for example
                double delta = basicEvent.getDetectionMethod().getDelta();
                int actualIndex = rawDataList.indexOf(rawData);
                timestamp = (long) rawData.getTimestamp();
                value = rawData.getValue();

                if (actualIndex + 1 < rawDataList.size()) {
                    Data nextData = rawDataList.get(actualIndex + 1);
                    double actualDifference = Math.abs(value - nextData.getValue());


                    if (actualDifference > maxDifference) {
                        date = TimestampUtils.formatToString(nextData.getTimestamp(), "dd-MM-yyyy HH:mm:ss");
                        response = "BasicEvent occured at " + date + " (from " + value + " to " + nextData.getValue() + ")";
                        BasicEventOccurrence basicEventOccurrence = new BasicEventOccurrence(basicEvent, date, value, nextData.getValue());

                        basicEventList.add(basicEventOccurrence);
                        System.out.println(response);
                    }
                }
            }
        } else {
            response = "Aucun évènement n'a pu être généré car aucune donnée brute n'a été reçue.";
        }

        return ok(views.html.basic.data.render("Your new application is ready.", basicEventList));
    }

}
