package controllers;

import com.avaje.ebean.Ebean;
import model.*;
import play.libs.Yaml;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import views.html.index;

import java.util.List;

/**
 * The main controller of the Play application.
 */
@Security.Authenticated(WebAuthentication.class)
public class Application extends Controller {

    /**
     * Initializes the controller with test data.
     *
     * @return the index result.
     */
    public static Result init() {
        Ebean.save((List) Yaml.load("test-data.yml"));

        return redirect(controllers.routes.LoginController.login());
    }

    /**
     * Loads the home page of the application.
     *
     * @return the index result.
     */
    public static Result index() {
        List<Sensor> sensors = Sensor.all();
        int newSensor = 0;
        int newAlert = 0;
        long now = System.currentTimeMillis() / 1000;
        int eventCount = EventOccurrence.find.where().between("timestamp",now - 24 * 3600, now).findRowCount();
        int basicCount = BasicEventOccurrence.find.where().between("timestamp",now - 24 * 3600, now).findRowCount();

        for(AlertOccurrence al : AlertOccurrence.all()) {
            if(!al.isSeen()) {
                newAlert++;
            }
        }
        for(Sensor sensor : sensors) {
            if(sensor.getDescription() == null) {
                newSensor++;
            }
        }
        return ok(index.render("Your new application is ready.", newSensor, AlertOccurrence.all(), newAlert, basicCount, eventCount));
    }
}
