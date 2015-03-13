package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.BasicEvent;
import model.BasicEventOccurrence;
import model.Sensor;
import model.json.Data;
import model.json.DataNode;
import play.mvc.Result;
import utils.TimestampUtils;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

/**
 * Controller to handle and display BasicEvent objects.
 * Created by Mathieu on 07/02/2015.
 */
public class BasicEventController {

    /**
     * Displays data about the basic events that occurred.
     *
     * @return the basic events page result.
     */
    public static Result data() {
        BasicEvent basicEvent = BasicEvent.byId("light_delta_15"); // fix the BasicEvent

        // Retrieve occurrences in play DB
        List<BasicEventOccurrence> basicEventList = BasicEventOccurrence.find.where().eq("basic_event_id", basicEvent.getId()).findList();

        String response = "Date,Occurrences\n";
        if (basicEventList != null && basicEventList.size() > 0) { // check for null result or empty list
            for (BasicEventOccurrence occurrence : basicEventList) {
                String simpleDate = TimestampUtils.formatToString(occurrence.getTimestamp(), "yyyy-MM-dd");
                response += simpleDate + ",1\n"; // add entry in response for each occurrence
            }
        } else {
            response = "Aucun évènement n'a pu être généré car aucune donnée brute n'a été reçue.";
        }

        if (request().getHeader("Accept").contains("text/csv")) {
            return ok(response);
        } else {
            return ok(views.html.basic.data.render("Your new application is ready.", basicEventList));
        }
    }

    public static Result graph() {
        return ok(views.html.basic.graph.render());
    }

    /**
     * Display the timeline of basicEventOccurrence.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on basicEvent
     */
    public static Result timeline(int page, String sortBy, String order, String filter, String amount, String begin, String end) {
//        List<BasicEventOccurrence> basicEventOccurrences = BasicEventOccurrence.all();
//        Collections.sort(basicEventOccurrences);
//        Collections.reverse(basicEventOccurrences);
        long beginTmp = 0, endTmp = 0; // timestamps in seconds
        boolean timeFilter = false;
        if(begin != null && begin.length() > 0) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
            try {
                cal.setTime(sdf.parse(begin));// all done
                beginTmp = cal.getTimeInMillis() / 1000;
                cal.setTime(sdf.parse(end));// all done
                endTmp = cal.getTimeInMillis() / 1000;
                timeFilter = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("Timefiler : " + timeFilter + " begin " + beginTmp + begin + " end " + endTmp + end);
        return timeFilter ?
                ok(views.html.basic.timeline.render("Évènements de base",
                        model.BasicEventOccurrence.pageTime(page, Integer.parseInt(amount), sortBy, order, filter, beginTmp, endTmp),
                        sortBy, order, filter, amount, begin, end,
                        BasicEvent.all()
                ))
                :
                ok(
                views.html.basic.timeline.render("Évènements de base",
                        model.BasicEventOccurrence.page(page, Integer.parseInt(amount), sortBy, order, filter),
                        sortBy, order, filter, amount, begin, end,
                        BasicEvent.all()
                )
        );
    }
}
