package controllers;

import model.*;
import play.mvc.Result;
import views.html.index;

import static play.mvc.Results.ok;

/**
 * Created by Ced on 31/01/2015.
 */
public class EventController {

    public static Result index() {
//        TimeInterval timeInterval = new TimeInterval();
//        timeInterval.setTimestampStart(1010000);
//        timeInterval.setTimestampEnd(1110000);
//        TimeInterval.create(timeInterval);
//
//        System.out.println(timeInterval.toString());
//
//        TimeInterval retrieveTimeInterval = TimeInterval.find.byId("1");
////        return ok(views.html.blank.render("Your new application is ready.", timeInterval.toString() +"    "+retrieveTimeInterval.toString()));
//        System.out.println(retrieveTimeInterval.toString() +"\n ----------------------------------------------- \n");
//
//        Detection detection = new Detection();
//        detection.setDelta(10);
//        Detection.create(detection);
//
//        System.out.println(detection.toString());
//        Detection retrieveDetection = Detection.find.byId("1");
//        System.out.println(retrieveDetection.toString() +"\n ----------------------------------------------- \n");
////
////        return ok(views.html.blank.render("Your new application is ready.", detection.toString() +"    "+retrieveDetection.toString()));
//
//        Sensor sensor = new Sensor();
//        sensor.setDescription("Ma description de capteur");
//        sensor.setLocation("Salle de bain");
//        sensor.setName("TelosB");
//        sensor.setType(SensorType.HUMIDITY);
//        Sensor.create(sensor);
//
//        System.out.println(sensor.toString());
//
//        Sensor retrieveSensor = Sensor.find.byId("TelosB");
//
//        System.out.println(retrieveSensor.toString() +"\n ----------------------------------------------- \n");
//
////        return ok(views.html.blank.render("Your new application is ready.", sensor.toString() +"    "+retrieveSensor.toString()));
//
//        BasicEvent basicEvent = new BasicEvent();
//        basicEvent.setId("My first Basic Event");
//        basicEvent.setBasicEventInterval(retrieveTimeInterval);
//        basicEvent.setDetectionMethod(retrieveDetection);
//        basicEvent.setSensor(retrieveSensor);
//        BasicEvent.create(basicEvent, retrieveTimeInterval.id, retrieveDetection.id, retrieveSensor.name);
//
        BasicEvent retrieveBasicEvent = BasicEvent.find.byId("My first Basic Event");
//
//        return ok(views.html.blank.render("Your new application is ready.", basicEvent.toString() +"    "+retrieveBasicEvent.toString()));
        return ok(views.html.blank.render("Your new application is ready.", retrieveBasicEvent.toString()));
    }

}
