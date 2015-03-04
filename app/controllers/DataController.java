package controllers;

import model.*;
import model.Data;
import model.json.DataNode;
import org.omg.CORBA.DATA_CONVERSION;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.GetDataFromUrl;
import utils.TimestampUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.ok;

/**
 * Controller to manage and display more complex events.
 * Created by Ced on 31/01/2015.
 */
public class DataController extends Controller {

    public static Result getUsers() {
        List<Data> users = Data.all();
        return ok(Json.toJson(users));
    }

    public static Result getUser(Long id) {
        Data user = Data.find.ref(null);
        return user == null ? null : ok(Json.toJson(user));
    }

    public static Result createUser() {
        Data newUser = Json.fromJson(request().body().asJson(), Data.class);
        Data inserted = Data.create(newUser);
        return created(Json.toJson(inserted));
    }

    public static Result updateUser(Long id) {
        Data user = Json.fromJson(request().body().asJson(), Data.class);
        //Data updated = Data.updateUser(id, user);
        return ok(Json.toJson(user));
        // TODO !
    }

    public static Result deleteUser(Long id) {
        // TODO
        //Database.deleteUser(id);
        return noContent(); // http://stackoverflow.com/a/2342589/1415732
    }
}
