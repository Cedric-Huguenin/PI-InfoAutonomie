package controllers;

import model.BasicEvent;
import model.BasicEventOccurrence;
import play.mvc.Result;
import utils.TimestampUtils;

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


    public static Result dataD3(String basicEventId) {
        BasicEvent basicEvent = BasicEvent.byId(basicEventId);
        List<BasicEventOccurrence> basicEventList = basicEventId.equals("") ? BasicEventOccurrence.all() : BasicEventOccurrence.find.where().eq("basic_event_id", basicEvent.getId()).findList();

        String response = "Date,Occurrences\n";
        if (basicEventList != null && basicEventList.size() > 0) { // check for null result or empty list
            for (BasicEventOccurrence occurrence : basicEventList) {
                String simpleDate = TimestampUtils.formatToString(occurrence.getTimestamp(), "yyyy-MM-dd");
                response += simpleDate + ",1\n"; // add entry in response for each occurrence
            }
        } else {
            response = "Aucun évènement n'a pu être généré car aucune donnée brute n'a été reçue.";
        }

        return ok(response);
    }

    /**
     * Displays data about the basic events that occurred.
     *
     * @return the basic events page result.
     */
    public static Result data(int page, String sortBy, String order, String basicEventId, String amount, String begin, String end) {

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

        BasicEvent basicEvent = BasicEvent.byId(basicEventId);

        // Retrieve occurrences in play DB
        List<BasicEventOccurrence> basicEventList = basicEventId.equals("") ? BasicEventOccurrence.all() : BasicEventOccurrence.find.where().eq("basic_event_id", basicEvent.getId()).findList();
        String name = basicEventId.equals("") ? "Tous" : basicEvent.getName();

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
            return timeFilter ?
                    ok(views.html.basic.data.render("Évènements de base",
                            model.BasicEventOccurrence.pageTime(page, Integer.parseInt(amount), sortBy, order, basicEventId, beginTmp, endTmp),
                            sortBy, order, basicEventId, amount, begin, end,
                            BasicEvent.all()
                    ))
                    :
                    ok(
                            views.html.basic.data.render("Évènements de base",
                                    model.BasicEventOccurrence.page(page, Integer.parseInt(amount), sortBy, order, basicEventId),
                                    sortBy, order, basicEventId, amount, begin, end,
                                    BasicEvent.all()
                            )
                    );
        }
    }

    public static Result graph(String basicEventId) {
        String name = basicEventId.equals("") ? "tous les basics" : BasicEvent.find.where().eq("id", basicEventId).findUnique().getName();
        return ok(views.html.basic.graph.render(name, basicEventId, BasicEvent.all()));
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
