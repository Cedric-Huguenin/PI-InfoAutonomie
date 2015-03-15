package controllers;

import model.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.data.Form;
import play.data.format.Formatters;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.alert.create;
import views.html.alert.alerts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static play.data.Form.form;

/**
 * Controller to manage and display alert.
 * Created by Ced on 31/01/2015.
 */
public class AlertController extends Controller {

    static Form<Alert> alertForm = Form.form(Alert.class);

    public static Result alerts() {
        List<Alert> allAlerts = Alert.all();

        return ok(alerts.render(allAlerts));
    }

    @With(WebAuthorization.class)
    public static Result edit(String id) {
        Alert alert = Alert.find.byId(id);
        alertForm.data().put("id", alert.getId());
        alertForm.data().put("name", alert.getName());
        alertForm.data().put("duration", alert.getDuration()+"");
        alertForm.data().put("expression", alert.getExpression());
        alertForm.data().put("color", alert.getColor());
        alertForm.data().put("icon", alert.getIcon());

        // TODO: template to edit an event
        return ok(create.render(alertForm, Event.all()));
    }

    @With(WebAuthorization.class)
    public static Result delete(String id) {
        Event event = Event.find.byId(id);
        event.delete();

        return redirect(controllers.routes.AlertController.alerts());
    }

    @With(WebAuthorization.class)
    public static Result save() {
        Form<Alert> alertForm = form(Alert.class).bindFromRequest();

        if (alertForm.hasErrors()) {
            return badRequest(create.render(alertForm, Event.all()));
        } else if (Alert.find.where().eq("id", alertForm.get().getId()).findRowCount() == 1) {
            Alert alert = Alert.find.ref(alertForm.get().id);

            alert.setName(alertForm.get().getName());
            alert.setExpression(alertForm.get().getExpression());
            alert.setColor(alertForm.get().getColor());
            alert.setIcon(alertForm.get().getIcon());

            alertForm.get().update();
        } else {
            alertForm.get().save();
        }

        return redirect(controllers.routes.AlertController.alerts());
    }

    /**
     * Loads the create event page.
     * @return the result of the event page.
     */
    @With(WebAuthorization.class)
    public static Result create() {
        return ok(create.render(form(Alert.class), Event.all()));
    }


    /**
     * Displays a list of more complex events (based on basic events) that occurred.
     * @return the result of the events occurred.
     */
    public static Result timeline(int page, String sortBy, String order, String filter, String amount, String begin, String end) {
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
                ok(views.html.alert.timeline.render("Alertes",
                        AlertOccurrence.pageTime(page, Integer.parseInt(amount), sortBy, order, filter, beginTmp, endTmp),
                        sortBy, order, filter, amount, begin, end,
                        Alert.all()
                ))
                :
                ok(
                        views.html.alert.timeline.render("Alertes",
                                AlertOccurrence.page(page, Integer.parseInt(amount), sortBy, order, filter),
                                sortBy, order, filter, amount, begin, end,
                                Alert.all()
                        )
                );
    }
}
