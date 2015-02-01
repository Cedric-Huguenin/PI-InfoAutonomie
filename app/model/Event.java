package model;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ced on 31/01/2015.
 */

@Entity
public class Event extends Model {

    @Id
    public String name;
    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<BasicEvent> basicEvents = new ArrayList<>();
    public int duration;
    @OneToOne
    public  TimeInterval timeInterval;

    public static Event create(Event event, String timeInterval) {
        event.timeInterval = TimeInterval.find.byId(timeInterval);
        event.save();
        event.saveManyToManyAssociations("basicEvents");
        event.save();
        return event;
    }

    public static Model.Finder<String,Event> find = new Model.Finder<>(String.class, Event.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BasicEvent> getBasicEvents() {
        return basicEvents;
    }

    public void setBasicEvents(List<BasicEvent> basicEvents) {
        this.basicEvents = basicEvents;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    public static Finder<String, Event> getFind() {
        return find;
    }

    public static void setFind(Finder<String, Event> find) {
        Event.find = find;
    }

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
