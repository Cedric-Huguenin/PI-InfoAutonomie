package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Motes;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result data() {
        // url to parse: http://iotlab.telecomnancy.eu/rest/data/1/light1/1
        ObjectMapper mapper = new ObjectMapper();
        // Motes motesNode = mapper.readValue(in, Motes.class);
        return ok(raw_values.render("Your new application is ready."));
    }

}
