package model;

import controllers.BasicEventOccurrenceController;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes an event.
 * Created by Ced on 31/01/2015.
 */

@Entity
public class Event extends Model {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    /**
     * The name of the event.
     */
    public String name;
    /**
     * List of the basic events composing the event.
     */
    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<BasicEvent> basicEvents = new ArrayList<>();
    /**
     * The duration of the event.
     */
    public int duration;
    /**
     * The time interval in which the event can happen.
     */
    @OneToOne
    public TimeInterval timeInterval;

    /**
     * The expression combining BasicEvent ids
     */
    public String expression;
    /**
     * The list of all the existing Event.
     */
    public static Model.Finder<String,Event> find = new Model.Finder<>(String.class, Event.class);

    /**
     * Initializes the given Event with the given time interval and saves it.
     * @param event the Event to initialize.
     * @param timeInterval a description of the TimeInterval.
     * @return the Event saved and initialized.
     */
    public static Event create(Event event, String timeInterval) {
        event.timeInterval = TimeInterval.find.byId(timeInterval);
        event.save();
        event.saveManyToManyAssociations("basicEvents");
        event.save();
        return event;
    }

    /**
     * Returns the list of all the Events
     * @return the list of all the Events
     */
    public static List<Event> all() {
        return find.all();
    }


    public void check() {
        BasicEventOccurrenceController basicEventOccurrenceController = new BasicEventOccurrenceController();

        String toEval = new String(expression);

        System.out.println("STRING : " + toEval);

        String[] basicEventIds = toEval.split("(\\|\\||&&)");
        for(String id : basicEventIds) {
            id = id.trim();
            BasicEvent basicEvent = BasicEvent.find.ref(id);
//            System.out.println("Current BasicEventID  : !" + basicEvent.getId());
            boolean occur = basicEventOccurrenceController.occur(timeInterval, basicEvent);
            toEval = toEval.replace(id, occur+"");

        }

        System.out.println("STRING : " + toEval);

        BooleanExpression boolExpr = null;
        try {
            boolExpr = BooleanExpression.readLeftToRight(toEval);
            boolean bool = boolExpr.booleanValue();
            // bool == true
            System.out.println(boolExpr.toString() + " == " + bool);
            // (((!true)&&false)||true) == true
        } catch (MalformedBooleanException e) {
            e.printStackTrace();
        }

        // TODO: evaluate
    }

    /**
     * Returns the name of the Event.
     * @return the name of the Event.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Event.
     * @param name the new Event name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of the BasicEvent used to identify this Event.
     * @return the list of the BasicEvent used to identify this Event.
     */
    public List<BasicEvent> getBasicEvents() {
        return basicEvents;
    }

    /**
     * Sets the list of the BasicEvent used to identify this Event.
     * @param basicEvents the new list.
     */
    public void setBasicEvents(List<BasicEvent> basicEvents) {
        this.basicEvents = basicEvents;
    }

    /**
     * Returns the duration of the Event.
     * @return the duration of the Event.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the Event.
     * @param duration the new duration.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Returns the TimeInterval in which the Event can occur.
     * @return the TimeInterval in which the Event can occur.
     */
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    /**
     * Sets the TimeInterval in which the Event can occur.
     * @param timeInterval the new TimeInterval.
     */
    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
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

    /**
     * Returns the description of the object as JSON.
     * @return the description of the object as JSON.
     */
    @Override
    public String toString() {
        String basicEventsStr = "";
        for(BasicEvent b : basicEvents) {
            basicEventsStr += b.toString() + " --- ";
        }
        return "Event{" +
                "name='" + name + '\'' +
                ", basicEvents=" + basicEventsStr +
                ", duration=" + duration +
                ", timeInterval=" + timeInterval +
                '}';
    }
}
