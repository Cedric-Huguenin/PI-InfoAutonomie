package controllers;

import model.Sensor;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller to handle and display raw data.
 * Created by Mathieu on 05/03/2015.
 */
public class RawDataController extends Controller {

    /**
     * Loads the page to display events.
     *
     * @return events results.
     */
    public static Result graph() {
        List<model.Data> dataList = model.Data.find.where().eq("mote", "153.111.LIGHT").findList();
        return ok(views.html.raw.graph.render("Your new application is ready.", dataList));
    }

    /**
     * Displays the live stream page with the raw data from the sensors in live.
     *
     * @return the live stream results.
     */
    public static Result liveStream() {
        return ok(views.html.raw.liveStream.render("Live stream"));
    }

    /**
     * Loads the JavaScript for the LiveStream.
     *
     * @return the JavaScript code to run the LiveStream.
     */
    public static Result liveStreamJS() {
        return ok(views.js.raw.liveStream.render());
    }

    /**
     * Display the paginated list of data.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on sensor
     */
    public static Result list(int page, String sortBy, String order, String filter, String amount, String begin, String end) {
        long beginTmp = 0, endTmp = 0; // timestamps in seconds
        boolean timeFilter = false;
        if (begin != null && begin.length() > 0) {
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

        return timeFilter ?
                ok(
                        views.html.raw.data.render(
                                model.Data.pageTime(page, Integer.parseInt(amount), sortBy, order, filter, beginTmp, endTmp),
                                sortBy, order, filter, amount, begin, end,
                                Sensor.all())
                )
                :
                ok(
                        views.html.raw.data.render(
                                model.Data.page(page, Integer.parseInt(amount), sortBy, order, filter),
                                sortBy, order, filter, amount, begin, end,
                                Sensor.all()
                        ));
    }
}
