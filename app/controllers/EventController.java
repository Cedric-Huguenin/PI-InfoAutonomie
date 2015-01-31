package controllers;

import play.mvc.Result;
import views.html.index;

import static play.mvc.Results.ok;

/**
 * Created by Ced on 31/01/2015.
 */
public class EventController {

    public static Result index() {
        return ok(views.html.blank.render("Your new application is ready.", "Blou"));
    }

}
