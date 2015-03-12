package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.APISecured;
import model.BasicEvent;
import model.Data;
import model.Event;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.ok;

/**
 * Controller to manage and display more complex events.
 * Created by Ced on 31/01/2015.
 */
public class DataController extends Controller {

    public static Result getAllData() {
        List<Data> dataList = Data.all();
        return ok(Json.toJson(dataList));
    }

    /**
     * Retrieve data from the database according to the given timestamp, data type and mote
     *
     * @param timestamp the timestamp
     * @param mote      the mote
     * @param label     the label
     * @return the data given by the mote for the label at the given timestamp
     */
    public static Data getDataIntern(Long timestamp, String mote, String label) {
        Data data = new Data(timestamp, 0, label, mote);
        data = Data.find.byId(data.getPrimKey());
        return data;
    }

    /**
     * Retrieve data from the database according to the given timestamp, data type and mote
     *
     * @param timestamp the timestamp
     * @param mote      the mote
     * @param label     the label
     * @return the data given by the mote for the label at the given timestamp
     */
    public static Result getData(Long timestamp, String mote, String label) {
        // GET http://localhost:9000/api/data/1411848808/219.98/temperature
        Data data = new Data(timestamp, 0, label, mote);
        data = Data.find.byId(data.getPrimKey());

        ObjectNode result = Json.newObject();
        if (data != null) {
            result.put("timestamp", data.getTimestamp());
            result.put("mote", data.getMote());
            result.put("label", data.getLabel());
            result.put("value", data.getValue());
        }
        return ok(result);
    }

    /**
     * Retrieve data from the database according to the given timestamp range, data type and mote
     *
     * @param begin begin of the range
     * @param end   end of the range
     * @param mote  data from this mote will be retrieved
     * @param label data with this label will be retrieved
     * @return the data list
     */
    public static Result getDataRange(Long begin, Long end, String mote, String label) {
        // GET http://localhost:9000/api/data/1411848800/14118488010/219.98/temperature
        List<Data> dataList = Data.find.where()
                .between("timestamp", begin, end).eq("mote", mote).eq("label", label).findList();
        return ok(Json.toJson(dataList));
    }

    /**
     * Retrieve data from the database according to the given timestamp range, data type and mote
     *
     * @param begin begin of the range
     * @param end   end of the range
     * @param mote  data from this mote will be retrieved
     * @param label data with this label will be retrieved
     * @return the data list
     */
    public static List<Data> getDataRangeIntern(Long begin, Long end, String mote, String label) {
        List<Data> dataList = Data.find.where()
                .between("timestamp", begin, end).eq("mote", mote).eq("label", label).findList();
        return dataList;
    }

    @Security.Authenticated(APISecured.class)
    public static Result createData() {
        // POST http://localhost:9000/api/data  {"timestamp":1411848808,"label":"temperature","value":24.0,"mote":"219.98.TEMP"}
        model.json.Data newData = Json.fromJson(request().body().asJson(), model.json.Data.class);
        // transform to Database Data (with primary key)
        Data inserted = Data.create(new Data(newData.getTimestamp(), newData.getValue(), newData.getLabel(), newData.getMote()));
        List<Event> eventsInvolved = new ArrayList<>();
        for(BasicEvent basic : BasicEvent.find.where().eq("sensor_id", newData.getMote()).findList()) {
            basic.check(); // check basic occurrence
            Event.find.where().ilike("expression", "%" + basic.getId() + "%").findList().stream().filter(event -> !eventsInvolved.contains(event)).forEach(eventsInvolved::add); // get Event containing this basic (check if already add)
        }
        eventsInvolved.forEach(model.Event::check); // check Event occurrence
        return created(Json.toJson(inserted));
    }

    @Security.Authenticated(APISecured.class)
    public static Result deleteData(Long timestamp, String mote, String label) {
        // DELETE http://localhost:9000/api/data/1411848808/219.98/temperature
        Data data = new Data(timestamp, 0, label, mote);
        Data.find.ref(data.getPrimKey()).delete();
        return ok();
    }
}
