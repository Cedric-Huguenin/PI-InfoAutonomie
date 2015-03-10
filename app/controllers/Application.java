package controllers;

import com.avaje.ebean.Ebean;
import model.Sensor;
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
    @Security.Authenticated(WebAuthentication.class)
    public static Result index() {
        List<Sensor> sensors = Sensor.all();
        int cpt = 0;
        for(Sensor sensor : sensors) {
            if(sensor.getDescription() == null) {
                cpt++;
            }
        }
        return ok(index.render("Your new application is ready.", cpt));
    }
}
