package controllers;

import model.*;
import model.json.Data;
import model.json.DataNode;
import play.mvc.Result;
import utils.GetDataFromUrl;
import utils.TimestampUtils;

import java.io.IOException;
import java.util.*;

import static play.mvc.Results.ok;

/**
 * Controller to manage and display more complex events.
 * Created by Ced on 31/01/2015.
 */
public class EventController {

    /**
     * Performs tests about sensors and events. TODO ? Place this in the unit tests
     * @return the test results.
     */
    public static Result test() {
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setTimestampStart(123456);
        timeInterval.setTimestampEnd(123789);
        TimeInterval.create(timeInterval);

        System.out.println(timeInterval.toString());
//
        TimeInterval retrieveTimeInterval = TimeInterval.find.byId("1");
////        return ok(views.html.blank.render("Your new application is ready.", timeInterval.toString() +"    "+retrieveTimeInterval.toString()));
        System.out.println(retrieveTimeInterval.toString() +"\n ----------------------------------------------- \n");
//
        Detection detection = new Detection();
        detection.setDelta(10);
        Detection.create(detection);
//
        System.out.println(detection.toString());
        Detection retrieveDetection = Detection.find.byId("1");
        System.out.println(retrieveDetection.toString() +"\n ----------------------------------------------- \n");
////
////        return ok(views.html.blank.render("Your new application is ready.", detection.toString() +"    "+retrieveDetection.toString()));
//
        Sensor sensor = new Sensor();
        sensor.setDescription("Ma description de capteur");
        sensor.setLocation("Salle de bain");
        sensor.setName("TelosB");
        sensor.setId("TelosB");
        sensor.setType(SensorType.HUMIDITY);
        Sensor.create(sensor);
//
        System.out.println(sensor.toString());
//
        Sensor retrieveSensor = Sensor.find.byId("TelosB");
//
        System.out.println(retrieveSensor.toString() +"\n ----------------------------------------------- \n");
//
////        return ok(views.html.blank.render("Your new application is ready.", sensor.toString() +"    "+retrieveSensor.toString()));
//
        BasicEvent basicEvent = new BasicEvent();
        basicEvent.setId("My first Basic Event");
        BasicEvent.create(basicEvent, retrieveDetection.getId(), retrieveSensor.getName());
//
        System.out.println(basicEvent.toString());
        BasicEvent retrieveBasicEvent = BasicEvent.find.byId("My first Basic Event");
        System.out.println(retrieveBasicEvent.toString() +"\n ----------------------------------------------- \n");
//
//        return ok(views.html.blank.render("Your new application is ready.", basicEvent.toString() +"    "+retrieveBasicEvent.toString()));

        Event event = new Event();
        event.setName("My first Event");
        event.getBasicEvents().add(retrieveBasicEvent);
        event.setTimeInterval(retrieveTimeInterval);

        Event.create(event, retrieveTimeInterval.id);

        Event retrieveEvent = Event.find.byId("My first Event");
//
        return ok(views.html.blank.render("Your new application is ready.", event.toString() +"   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!   "+ retrieveEvent.toString()));
    }

    /**
     * Displays a list of more complex events (based on basic events) that occurred.
     * @return the result of the events occurred.
     */
    public static Result listEvents() {
        return ok(views.html.eventTimeline.render("Évènement", EventOccurrence.all()));
    }

    /**
     * Computes the basic events to determine if a more complex event occurred.
     * @return the result of the occurrences in a timeline.
     */
    public static Result index() {
        List<BasicEvent> basics = BasicEvent.all();
        List<BasicEventOccurrence> events = new ArrayList<>();

        for(BasicEvent basic : basics) {

            String typeStr = "";
            if (basic.getSensor().getType() == SensorType.LIGHT) {
                typeStr = "light1";
            } else if (basic.getSensor().getType() == SensorType.TEMP) {
                typeStr = "temperature";
            }
            String lightUrl = "http://iotlab.telecomnancy.eu/rest/data/1/" + typeStr + "/100/" + basic.getSensor().getAddress();
            System.out.println(lightUrl);

            DataNode dataNode = null;
            try {
                dataNode = GetDataFromUrl.getFromUrl(lightUrl);
            } catch (IOException e) { // TODO: return error on web page
                e.printStackTrace();
            }

            Data oldData = null;
            System.out.println(dataNode.getData().size());
            for (Data data : dataNode.getData()) {
                if (oldData != null) {
                    if (Math.abs(oldData.getValue() - data.getValue()) >= basic.getDetectionMethod().getDelta()) {
                        events.add(new BasicEventOccurrence(basic, TimestampUtils.formatToString(data.getTimestamp(), "dd-MM-yyyy HH:mm:SS"), data.getTimestamp(), oldData.getValue(), data.getValue()));
                    }
                }
                oldData = data;
            }

//        System.out.println(basic);
//        System.out.println(events.size());
            Collections.sort(events);
            Collections.reverse(events);
        }
        return ok(views.html.timeline.render("Évènement", events));
    }
}
