package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Data;
import model.Sensor;
import model.SensorType;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Controller to manage and display more complex events.
 * Created by Ced on 31/01/2015.
 */
public class SensorController extends Controller {

    public static Result getMotes() {
        List<Sensor> dataList = Sensor.all();
        return ok(Json.toJson(dataList));
    }

    public static Result getMote(String sensorId) {
        // GET http://localhost:9000/api/sensor/sensorId
        Sensor sensor = Sensor.find.byId(sensorId);

        return ok(Json.toJson(sensor));
    }

    public static Result createSensor() {
        // POST http://localhost:9000/api/sensor  {"address":"153.111","type":"LIGHT"}
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String address = json.findPath("address").asText();
            String type = json.findPath("type").asText();
            if(address == null || type == null) {
                return badRequest("Missing parameter [name]");
            } else {
                Sensor sensor = new Sensor();
                sensor.setId(address+"."+type);
                sensor.setAddress(address);
                switch(type) {
                    case "DOOR":
                        sensor.setType(SensorType.DOOR);
                        break;
                    case "LIGHT":
                        sensor.setType(SensorType.LIGHT);
                        break;
                    case "TEMP":
                        sensor.setType(SensorType.TEMP);
                        break;
                    case "HUMIDITY":
                        sensor.setType(SensorType.HUMIDITY);
                        break;
                    case "POWER":
                        sensor.setType(SensorType.POWER);
                        break;
                    case "PRESENCE":
                        sensor.setType(SensorType.PRESENCE);
                        break;
                }
                Sensor.create(sensor);
                return created(Json.toJson(sensor));
            }
        }
    }

    public static Result deleteSensor(String sensorId) {
        // DELETE http://localhost:9000/api/sensor/sensorId
        Sensor.find.ref(sensorId).delete();
        return ok();
    }
}
