package model;

import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import play.data.format.Formats;
import play.db.ebean.Model;
import utils.TimestampUtils;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Describes an event.
 * Created by Ced on 31/01/2015.
 */

@Entity
public class Event extends Model {

    @Id
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

    @Formats.DateTime(pattern = "HH:mm")
    public DateTime beginTime;

    @Formats.DateTime(pattern = "HH:mm")
    public DateTime endTime;

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
        String[] basicEventIds = matches.toArray(new String[matches.size()]);

        // first, check if all given basic events exist in database
        for (String id : basicEventIds) {
            id = id.trim();
            toEval = toEval.replace(id, "true");
            boolean basicEventExist = BasicEvent.find.where().eq("id", id).findRowCount() == 1;
            System.out.println("--- basicEventID: " + id + " " + basicEventExist + " ---");
            if (!basicEventExist) {
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
     * The list of all the existing Event.
     */
    public static Model.Finder<String, Event> find = new Model.Finder<>(String.class, Event.class);

    /**
     * Initializes the given Event with the given time interval and saves it.
     *
     * @param event        the Event to initialize.
     * @param timeInterval a description of the TimeInterval.
     * @return the Event saved and initialized.
     */
    public static Event create(Event event, String timeInterval) {
//        event.timeInterval = TimeInterval.find.byId(timeInterval);
        event.setId(event.getName().replaceAll(" ", "_").toLowerCase());
        event.save();
        event.saveManyToManyAssociations("basicEvents");
        event.save();
        return event;
    }

    /**
     * Returns the list of all the Events
     *
     * @return the list of all the Events
     */
    public static List<Event> all() {
        return find.all();
    }


    public void check() {
        // TODO: verify that event has not already been detected for the last TimeInterval
        long[] todayTimeInterval = getActualTimeInterval();
        if (EventOccurrence.find.where().eq("event_id", getId())
                .between("timestamp", todayTimeInterval[0], todayTimeInterval[1])
                .findIds().size() > 0) { // Event already in DB
            return;
        }

        BasicEventOccurrence basicEventOccurrence = new BasicEventOccurrence();

        String toEval = expression; // copy the string

//        System.out.println("STRING : " + toEval);
        long mean = 0;
        int cpt = 0;

        // fetch the ids of the basic events composing the event
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(toEval);

        List<String> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.group());
        }
        String[] basicEventIds = matches.toArray(new String[matches.size()]);

        List<long[]> timeIntervals = new ArrayList<>();
        timeIntervals.add(todayTimeInterval);

        if (EventOccurrence.find.where().eq("event_id", getId()).findList().size() == 0 && BasicEventOccurrence.find.findRowCount() > 0) { // no Event occurrence in DB
                                                                                                            // but BasicEventOccurrence in DB
            // find the oldest BasicEventOccurrence
            // compute for each day between now and this date

            long oldestBasicOccurrence =
                    BasicEventOccurrence.find.where().orderBy("timestamp ascending").
                            findPagingList(1).getPage(1)
                            .getList().get(0).getTimestamp();
            System.out.println(oldestBasicOccurrence);

            DateTime firstBasic = new DateTime(oldestBasicOccurrence*1000); // needs milliseconds
            DateTime now = new DateTime(System.currentTimeMillis());
            int dayBetween = now.get(DateTimeFieldType.dayOfYear()) - firstBasic.get(DateTimeFieldType.dayOfYear());

            for(int i = 0; i < dayBetween; i++) {
                long[] timeInterval = new long[2];

                timeInterval[0] = todayTimeInterval[0] - (i+1)*24*3600;
                timeInterval[1] = todayTimeInterval[1] - (i+1)*24*3600;
                timeIntervals.add(timeInterval);
            }

            // DEBUG
//            for(int i = 0; i<timeIntervals.size(); i++) {
//                System.out.println(timeIntervals.get(i)[0] + " " + TimestampUtils.formatToString(timeIntervals.get(i)[0], "dd-MM-yyyy HH:mm:SS"));
//            }

        }

        for (long[] timeInterval : timeIntervals) {
            // replace the ids by true or false
            toEval = expression;
//            System.out.println("\n\nSTRING : " + toEval);
            for (String id : basicEventIds) {
                id = id.trim();
                BasicEvent basicEvent = BasicEvent.find.ref(id);
//                System.out.println("Current BasicEventID: " + basicEvent.getId());
                long occurTime = basicEventOccurrence.occur(timeInterval, basicEvent);
                if (occurTime != -1) {
                    cpt++;
                    mean += occurTime;
                }
                toEval = toEval.replace(id, (occurTime != -1) + "");
            }

//            System.out.println("STRING : " + toEval);

            BooleanExpression boolExpr;
            try {
                boolExpr = BooleanExpression.readLeftToRight(toEval);
                boolean bool = boolExpr.booleanValue();
//                System.out.println(boolExpr.toString() + " == " + bool);

                if (bool && cpt > 0) {
                    EventOccurrence eventOccurrence = new EventOccurrence(this, mean / cpt, TimestampUtils.formatToString(mean / cpt, "dd-MM-yyyy HH:mm:ss"));
                    if (EventOccurrence.find.where().eq("timestamp", eventOccurrence.getTimestamp()).eq("event_id", eventOccurrence.getEvent().getId()).findUnique() == null) {
                        eventOccurrence.save();
                    }
                }
            } catch (MalformedBooleanException e) {
                e.printStackTrace();
            }
        }
    }

    public long[] getActualTimeInterval() {
        long[] res = new long[2];
        res[0] = getTimestampStart();
        res[1] = getTimestampEnd(res[0]);
        return res;
    }

    public long getTimestampStart() {
        GregorianCalendar start = setCalWithLocalTime(beginTime);

        if (start.getTimeInMillis() > System.currentTimeMillis()) { // it's 1:00 and event begin at 6 for example
            return start.getTimeInMillis() / 1000 - 24 * 3600; // subtract on day in second
        } else {
            return start.getTimeInMillis() / 1000;
        }
    }


    public long getTimestampEnd(long begin) {
        long end = begin + (getEndTime().getMillis() - getBeginTime().getMillis()) / 1000;
        return end > begin ? end : end + 3600*24;
    }

    private GregorianCalendar setCalWithLocalTime(DateTime localTime) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, localTime.get(DateTimeFieldType.clockhourOfDay()));
        cal.set(Calendar.MINUTE, localTime.get(DateTimeFieldType.minuteOfHour()));
        cal.set(Calendar.SECOND, 0);

        return cal;
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
     * Returns the list of the BasicEvent used to identify this Event.
     *
     * @return the list of the BasicEvent used to identify this Event.
     */
    public List<BasicEvent> getBasicEvents() {
        return basicEvents;
    }

    /**
     * Sets the list of the BasicEvent used to identify this Event.
     *
     * @param basicEvents the new list.
     */
    public void setBasicEvents(List<BasicEvent> basicEvents) {
        this.basicEvents = basicEvents;
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

    public DateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(DateTime beginTime) {
        this.beginTime = beginTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the description of the object as JSON.
     *
     * @return the description of the object as JSON.
     */
    @Override
    public String toString() {
        String basicEventsStr = "";
        for (BasicEvent b : getBasicEvents()) {
            basicEventsStr += b.toString() + " --- ";
        }
        return "Event{" +
                "name='" + getName() + '\'' +
                ", basicEvents=" + basicEventsStr +
                ", duration=" + getDuration() +
                '}';
    }
}
