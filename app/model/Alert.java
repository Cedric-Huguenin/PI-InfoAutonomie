package model;

import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import play.data.format.Formats;
import play.db.ebean.Model;
import utils.TimestampUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Describes an alert.
 * Created by Ced on 31/01/2015.
 */

@Entity
public class Alert extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    /**
     * The name of the alert.
     */
    public String name;

    @ManyToOne(cascade = CascadeType.REMOVE)
    public BasicEvent startBasicEvent;

    @ManyToOne(cascade = CascadeType.REMOVE)
    public Event startEvent;

    /**
     * The duration (in minutes) during which the expression has to be verified to trigger the alert.
     */
    public int duration;

    /**
     * The expression combining BasicEvent ids
     */
    public String expression;

    public String icon;

    public String color;

    public String validate() {
        if (expression.isEmpty()) {
            return "Expression vide !";
        }
        if (!validateExpression(expression)) {
            return "Expression fausse ou un évènement de base n'existe pas.";
        }
        return null;
    }

    public boolean validateExpression(String toEval) {
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(toEval);

        List<String> matches = new ArrayList<String>();
        while (m.find()) {
            matches.add(m.group());
        }
        String[] eventIds = matches.toArray(new String[matches.size()]);

        // first, check if all given basic events exist in database
        for (String id : eventIds) {
            id = id.trim();
            toEval = toEval.replace(id, "true");
            boolean eventExist = Event.find.where().eq("id", id).findRowCount() == 1;
            if (!eventExist) {
                eventExist = BasicEvent.find.where().eq("id", id).findRowCount() == 1;
            }
            System.out.println("--- basicEventID: " + id + " " + eventExist + " ---");
            if (!eventExist) {
                return false; // but continue if true
            }
        }

        // then, test if the expression is syntactically correct by trying to parse it (after having replacer all the ids by true for example
        BooleanExpression boolExpr;
        try {
            System.out.println("Expression: " + toEval);
            boolExpr = BooleanExpression.readLeftToRight(toEval);
            boolean bool = boolExpr.booleanValue();
            System.out.println("Result of the evaluation: " + boolExpr + " == " + bool);
        } catch (MalformedBooleanException e) {
            System.err.println("Invalid Expression");
//            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void check() {

        long[] timeInterval = new long[2];

        long now = System.currentTimeMillis() / 1000;

        if (startBasicEvent != null) {
            try {
                timeInterval[0] = BasicEventOccurrence.find.where()
                        .eq("basic_event_id", startBasicEvent.getId())
                        .orderBy("timestamp descending").
                                findPagingList(1).getPage(1)
                        .getList().get(0).getTimestamp();
            } catch (Exception e) {
                return;
            }
        } else if (startEvent != null) {
            try {
                timeInterval[0] = EventOccurrence.find.where()
                        .eq("event_id", startEvent.getId())
                        .orderBy("timestamp descending").
                                findPagingList(1).getPage(1)
                        .getList().get(0).getTimestamp();
            } catch (Exception e) {
                return;
            }
        } else {
            return;
        }

        timeInterval[1] = timeInterval[0] + getDuration() * 60;
        String toEval = expression;

        if(expression.contains("!")) { // alert based on the absence of event occurrence
            // must wait for the duration to detect if actually, the event does not occur
            if(now < timeInterval[1]) {
                return;
            }
        }

        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(toEval);

        List<String> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.group());
        }
        String[] ids = matches.toArray(new String[matches.size()]);

        for (String id : ids) {
            id = id.trim();
            BasicEvent basic = BasicEvent.find.where().eq("id", id).findUnique();
            long occurTime;
            if (basic != null) {
                //System.out.println("TimetoCheck alert : " + TimestampUtils.formatToString(timeInterval[0], "dd-MM-yyyy HH:mm:ss") + "->" + TimestampUtils.formatToString(timeInterval[1], "dd-MM-yyyy HH:mm:ss") );
                occurTime = BasicEventOccurrence.occur(timeInterval, basic);
                toEval = toEval.replace(id, (occurTime != -1) + "");
            } else { // it's an Event
                Event event = Event.find.where().eq("id", id).findUnique();
                occurTime = EventOccurrence.occur(timeInterval, event);
                toEval = toEval.replace(id, (occurTime != -1) + "");
            }
        }

        BooleanExpression boolExpr;
        try {
            boolExpr = BooleanExpression.readLeftToRight(toEval);
            boolean bool = boolExpr.booleanValue();
//                System.out.println("Alerte : " + boolExpr.toString() + " == " + bool);

            if (bool) {
                AlertOccurrence alertOccurrence = new AlertOccurrence(this, now, TimestampUtils.formatToString(now, "dd-MM-yyyy HH:mm:ss"));
                if (AlertOccurrence.find.where().eq("timestamp", alertOccurrence.getTimestamp()).eq("alert_id", alertOccurrence.getAlert().getId()).findUnique() == null) {
                    alertOccurrence.save();
                }
            }
        } catch (MalformedBooleanException e) {
            e.printStackTrace();
        }
    }

    /**
     * The list of all the existing alert.
     */
    public static Finder<String, Alert> find = new Finder<>(String.class, Alert.class);

    /**
     * Initializes the given alert with the given time interval and saves it.
     *
     * @param alert the Event to initialize.
     * @return the Event saved and initialized.
     */
    public static Alert create(Alert alert) {
        alert.save();
        return alert;
    }

    /**
     * Returns the list of all the Events
     *
     * @return the list of all the Events
     */
    public static List<Alert> all() {
        return find.all();
    }


    /**
     * Returns the name of the Event.
     *
     * @return the name of the Event.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Event.
     *
     * @param name the new Event name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the duration of the Event.
     *
     * @return the duration of the Event.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the Event.
     *
     * @param duration the new duration.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BasicEvent getStartBasicEvent() {
        return startBasicEvent;
    }

    public void setStartBasicEvent(BasicEvent startBasicEvent) {
        this.startBasicEvent = startBasicEvent;
    }

    public Event getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(Event startEvent) {
        this.startEvent = startEvent;
    }

    /**
     * Returns the description of the object as JSON.
     *
     * @return the description of the object as JSON.
     */
    @Override
    public String toString() {
        return "Alert{" +
                "name='" + getName() + '\'' +
                ", duration=" + getDuration() +
                ", expression=" + getExpression() +
                '}';
    }
}
