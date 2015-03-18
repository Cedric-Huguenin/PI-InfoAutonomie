package controllers;

import model.Event;
import model.Sensor;

import static play.data.Form.*;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import views.html.sensor.sensors;
import views.html.sensor.editSensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller to manage and display more complex events.
 * Created by Ced on 31/01/2015.
 */
@Security.Authenticated(WebAuthentication.class)
public class SensorController extends Controller {

    public static Result sensors() {

        List<Sensor> allSensors = Sensor.all();
        List<Sensor> oldSensors = new ArrayList<>();
        List<Sensor> newSensors = new ArrayList<>();

        for (Sensor sensor : allSensors) {
            if (sensor.getDescription() == null) {
                newSensors.add(sensor);
            } else {
                oldSensors.add(sensor);
            }
        }
        return ok(sensors.render(oldSensors, newSensors));
    }

    public static Result sensor(String id) {
        Form<Sensor> form = form(Sensor.class);
        Sensor sensor = Sensor.find.byId(id);
        //System.out.println("List: id "+ id + " " + sensor);
        form.data().put("id", sensor.getId());
        form.data().put("name", sensor.getName());
        form.data().put("address", sensor.getAddress());
        form.data().put("location", sensor.getLocation());
        form.data().put("description", sensor.getDescription());
        form.data().put("unit", sensor.getUnit());
        form.data().put("type", sensor.getTypeAsString());

        return ok(editSensor.render(form));
    }

    @With(WebAuthorization.class)
    public static Result updateSensor() {
        Form<Sensor> sensorForm = form(Sensor.class).bindFromRequest();

        //System.out.println("\n\n\nForm : name " + sensorForm.get().getName() + sensorForm.get().getAddress()+"\n\n\n");
        Sensor sensor = Sensor.find.ref(sensorForm.get().id);
        sensor.setDescription(sensorForm.get().getDescription());
        sensor.setLocation(sensorForm.get().getLocation());
        sensor.setName(sensorForm.get().getName());
        sensor.setUnit(sensorForm.get().getUnit());

        sensor.update();

        return redirect(controllers.routes.SensorController.sensors());
    }

    @With(WebAuthorization.class)
    public static Result resetSensor(String id) {
        Sensor sensor = Sensor.find.byId(id);
        sensor.setDescription(null);
        sensor.setName(null);
        sensor.setLocation(null);
        sensor.update();

        return redirect(controllers.routes.SensorController.sensors());
    }

    @With(WebAuthorization.class)
    public static Result delete(String id) {
        Sensor sensor = Sensor.find.byId(id);
        sensor.delete();

        return redirect(controllers.routes.SensorController.sensors());
    }
}
