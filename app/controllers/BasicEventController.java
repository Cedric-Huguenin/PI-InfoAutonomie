package controllers;

import model.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.TimestampUtils;
import views.html.basic.basics;
import views.html.basic.create;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static play.data.Form.form;
/**
 * Controller to handle and display BasicEvent objects.
 * Created by Mathieu on 07/02/2015.
 */
public class BasicEventController extends Controller {

    public static Result basics() {
        List<BasicEvent> allBasicEvents = BasicEvent.all();

        return ok(basics.render(allBasicEvents));
    }

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
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on basicEvent
     */
    public static Result timeline(int page, String sortBy, String order, String filter, String amount, String begin, String end) {
//        List<BasicEventOccurrence> basicEventOccurrences = BasicEventOccurrence.all();
//        Collections.sort(basicEventOccurrences);
//        Collections.reverse(basicEventOccurrences);
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

    @With(WebAuthorization.class)
    public static Result create() {
        Form<BasicEvent> form = form(BasicEvent.class);
        form.data().put("type", "");
        form.data().put("sensor", "");
        return ok(create.render(form, Sensor.all(), Detection.all()));
    }

    @With(WebAuthorization.class)
    public static Result delete(String id) {
        Event event = Event.find.byId(id);
        event.delete();

        return redirect(controllers.routes.EventController.events());
    }

    @With(WebAuthorization.class)
    public static Result edit(String id) {
        Form<BasicEvent> eventForm = Form.form(BasicEvent.class);
        BasicEvent event = BasicEvent.find.byId(id);
        eventForm.data().put("id", event.getId());
        eventForm.data().put("name", event.getName());
        eventForm.data().put("color", event.getColor());
        eventForm.data().put("icon", event.getIcon());
        eventForm.data().put("sensor", event.getSensor().getId());

        switch (event.getDetectionMethod().getDetectionType()){
            case DELTA:
                eventForm.data().put("type", "delta");
                eventForm.data().put("delta", event.getDetectionMethod().getDelta()+"");
                break;
            case SIMPLE_THRESHOLD:
                eventForm.data().put("type", "threshold");
                eventForm.data().put("threshold", event.getDetectionMethod().getSimpleThreshold()+"");
                break;
            case MIN_MAX_VALUES:
                eventForm.data().put("type", "min_max");
                eventForm.data().put("min", event.getDetectionMethod().getMinValue()+"");
                eventForm.data().put("max", event.getDetectionMethod().getMaxValue()+"");
                break;
        }

        return ok(create.render(eventForm, Sensor.all(), Detection.all()));
    }

    @With(WebAuthorization.class)
    public static Result save() {

        Form<BasicEvent> eventForm = form(BasicEvent.class).bindFromRequest();

        if (Event.find.where().eq("id", eventForm.get().getId()).findRowCount() == 1) {
            BasicEvent basic = BasicEvent.find.ref(eventForm.get().id);

            Detection detection = basic.getDetectionMethod();

            switch(eventForm.data().get("type")) {
                case "delta":
                    detection.setDetectionType(DetectionType.DELTA);
                    detection.setDelta(Double.parseDouble(eventForm.data().get("delta")));
                    break;
                case "simple_threshold":
                    detection.setDetectionType(DetectionType.SIMPLE_THRESHOLD);
                    detection.setSimpleThreshold(Double.parseDouble(eventForm.data().get("threshold")));
                    break;
                case "min_max":
                    detection.setDetectionType(DetectionType.MIN_MAX_VALUES);
                    detection.setMinValue(Double.parseDouble(eventForm.data().get("min")));
                    detection.setMaxValue(Double.parseDouble(eventForm.data().get("max")));
                    break;
            }

            detection.save();

            basic.setName(eventForm.get().getName());
            basic.setColor(eventForm.get().getColor());
            basic.setIcon(eventForm.get().getIcon());
            basic.setSensor(eventForm.get().getSensor());


            basic.check();
            eventForm.get().update();
        } else {
            BasicEvent basic = new BasicEvent();
            basic.setName(eventForm.get().getName());
            basic.setColor(eventForm.get().getColor());
            basic.setIcon(eventForm.get().getIcon());

            Detection detection = new Detection();
            switch(eventForm.data().get("type")) {
                case "delta":
                    detection.setDetectionType(DetectionType.DELTA);
                    detection.setDelta(Double.parseDouble(eventForm.data().get("delta")));
                    break;
                case "simple_threshold":
                    detection.setDetectionType(DetectionType.SIMPLE_THRESHOLD);
                    detection.setSimpleThreshold(Double.parseDouble(eventForm.data().get("threshold")));
                    break;
                case "min_max":
                    detection.setDetectionType(DetectionType.MIN_MAX_VALUES);
                    detection.setMinValue(Double.parseDouble(eventForm.data().get("min")));
                    detection.setMaxValue(Double.parseDouble(eventForm.data().get("max")));
                    break;
            }

            detection.save();

            BasicEvent.create(basic, detection.getId(), eventForm.get().getSensor().getId());
            basic.check();
        }

        return redirect(controllers.routes.BasicEventController.basics());
    }
}
