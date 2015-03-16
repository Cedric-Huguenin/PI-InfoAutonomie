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
public class AlertOccurrence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    @ManyToOne
    public Alert alert;

    public long timestamp;
    public String date;

    public boolean seen;

    public AlertOccurrence(Alert alert, long timestamp, String date) {
        this.alert = alert;
        this.timestamp = timestamp;
        this.date = date;
        this.seen = false;
    }

    public static Finder<String, AlertOccurrence> find = new Finder<>(String.class, AlertOccurrence.class);

    public static List<AlertOccurrence> all() {
        return find.all();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public static Page<AlertOccurrence> page(int page, int pageSize, String sortBy, String order, String filter) {
        return
                find.where()
                        .ilike("alert_id", "%" + filter + "%")
                        .orderBy(sortBy+ " " + order)
                                //.fetch("company")
                        .findPagingList(pageSize)
                        .setFetchAhead(false)
                        .getPage(page);
    }

    public static Page<AlertOccurrence> pageTime(int page, int pageSize, String sortBy, String order, String filter, long beginTmp, long endTmp) {
        return
                find.where()
                        .between("timestamp", beginTmp, endTmp)
                        .ilike("alert_id", "%" + filter + "%")
                        .orderBy(sortBy+ " " + order)
                                //.fetch("company")
                        .findPagingList(pageSize)
                        .setFetchAhead(false)
                        .getPage(page);
    }
}
