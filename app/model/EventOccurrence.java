package model;

import com.avaje.ebean.Page;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Event Occurrence model
 * Persistent object
 */

@Entity
public class EventOccurrence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    @ManyToOne
    public Event event;

    public long timestamp;
    public String date;

    public EventOccurrence() {
    }

    public EventOccurrence(Event event, long timestamp, String date) {
        this.event = event;
        this.timestamp = timestamp;
        this.date = date;
    }

    public static Model.Finder<String,EventOccurrence> find = new Model.Finder<>(String.class, EventOccurrence.class);

    public static List<EventOccurrence> all() {
        return find.all();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static Page<EventOccurrence> page(int page, int pageSize, String sortBy, String order, String filter) {
        return
                find.where()
                        .ilike("event_id", "%" + filter + "%")
                        .orderBy(sortBy+ " " + order)
                                //.fetch("company")
                        .findPagingList(pageSize)
                        .setFetchAhead(false)
                        .getPage(page);
    }

    public static Page<EventOccurrence> pageTime(int page, int pageSize, String sortBy, String order, String filter, long beginTmp, long endTmp) {
        return
                find.where()
                        .between("timestamp", beginTmp, endTmp)
                        .ilike("event_id", "%" + filter + "%")
                        .orderBy(sortBy+ " " + order)
                                //.fetch("company")
                        .findPagingList(pageSize)
                        .setFetchAhead(false)
                        .getPage(page);
    }

    public static long occur(long[] t, Event event) {
//        System.out.println("From " + t[0] + " to " + t[1]);
//        System.out.println("SEARCHING FOR " + basicEvent.getId());

        List<EventOccurrence> eventOccurrences = EventOccurrence.find.where()
                .between("timestamp", t[0], t[1]).eq("basic_event_id", event.getId()).findList();

//        System.out.println("Found " + basicsEventOccurrences.size() + " item(s) ------ ");


        return eventOccurrences.size() > 0 ? eventOccurrences.get(0).getTimestamp() : -1;
    }
}
