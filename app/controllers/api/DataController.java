package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Secured;
import model.*;
import model.Data;
import model.json.DataNode;
import org.omg.CORBA.DATA_CONVERSION;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
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

    public static Result getAllData() {
        List<Data> dataList = Data.all();
        return ok(Json.toJson(dataList));
    }

    public static Data getDataIntern(Long timestamp, String mote, String label) {
        Data data = new Data(timestamp, 0, label, mote);
        data = Data.find.byId(data.getPrimKey());
        return data;
    }

    public static Result getData(Long timestamp, String mote, String label) {
        // GET http://localhost:9000/api/data/1411848808/219.98/temperature
        Data data = new Data(timestamp, 0, label, mote);
        data = Data.find.byId(data.getPrimKey());

        ObjectNode result = Json.newObject();
        if(data != null) {
            result.put("timestamp", data.getTimestamp());
            result.put("mote", data.getMote());
            result.put("label", data.getLabel());
            result.put("value", data.getValue());
        }
        return ok(result);
    }

    public static Result getDataRange(Long begin, Long end, String mote, String label) {
        // GET http://localhost:9000/api/data/1411848800/14118488010/219.98/temperature
        List<Data> dataList = Data.find.where()
                .between("timestamp", begin, end).eq("mote", mote).eq("label", label).findList();
        return ok(Json.toJson(dataList));
    }

    public static List<Data> getDataRangeIntern(Long begin, Long end, String mote, String label) {
        List<Data> dataList = Data.find.where()
                .between("timestamp", begin, end).eq("mote", mote).eq("label", label).findList();
        return dataList;
    }

    @Security.Authenticated(Secured.class)
    public static Result createData() {
        // POST http://localhost:9000/api/data  {"timestamp":1411848808,"label":"temperature","value":24.0,"mote":"219.98"}
        model.json.Data newData = Json.fromJson(request().body().asJson(), model.json.Data.class);
        // transform to Database Data (with primary key)
        Data inserted = Data.create(new Data(newData.getTimestamp(), newData.getValue(), newData.getLabel(), newData.getMote()));
        return created(Json.toJson(inserted));
    }

    @Security.Authenticated(Secured.class)
    public static Result deleteData(Long timestamp, String mote, String label) {
        // DELETE http://localhost:9000/api/data/1411848808/219.98/temperature
        Data data = new Data(timestamp, 0, label, mote);
        Data.find.ref(data.getPrimKey()).delete();
        return ok();
    }
}
