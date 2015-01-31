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
        event.timeInterval = TimeInterval.find.ref(timeInterval);
        event.saveManyToManyAssociations("basicEvents");
        event.save();
        return event;
    }

    public static Model.Finder<String,Event> find = new Model.Finder<>(String.class, Event.class);

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", basicEvents=" + basicEvents +
                ", duration=" + duration +
                ", timeInterval=" + timeInterval +
                '}';
    }
}
