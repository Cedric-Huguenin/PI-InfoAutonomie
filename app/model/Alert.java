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

    /**
     * The duration of the alert.
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
            System.out.println("--- basicEventID: " + id + " " + eventExist + " ---");
            if (!eventExist) {
                return false;
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

    /**
     * The list of all the existing alert.
     */
    public static Finder<String, Alert> find = new Finder<>(String.class, Alert.class);

    /**
     * Initializes the given alert with the given time interval and saves it.
     *
     * @param alert        the Event to initialize.
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
