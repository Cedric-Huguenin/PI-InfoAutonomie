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

    public static Result createData() {
        // POST http://localhost:9000/data  {"timestamp":1411848808,"label":"temperature","value":24.0,"mote":"219.98"}
        model.json.Data newData = Json.fromJson(request().body().asJson(), model.json.Data.class);
        // transform to Database Data (with primary key)
        Data inserted = Data.create(new Data(newData.getTimestamp(), newData.getValue(), newData.getLabel(), newData.getMote()));
        return created(Json.toJson(inserted));
    }

    public static Result updateUser(Long id) {
        Data user = Json.fromJson(request().body().asJson(), Data.class);
        //Data updated = Data.updateUser(id, user);
        return ok(Json.toJson(user));
        // TODO !
    }

    public static Result deleteData(Long timestamp, String mote, String label) {
        // DELETE http://localhost:9000/data/1411848808/219.98/temperature
        Data data = new Data(timestamp, 0, label, mote);
        Data.find.ref(data.getPrimKey()).delete();
        return ok();

    }
}
