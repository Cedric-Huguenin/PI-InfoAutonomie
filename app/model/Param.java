package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Parameters
 */
@Entity
public class Param extends Model {
    @Id
    public String paramKey;

    public String paramValue;

    public static Finder<String, Param> find = new Finder<>(String.class, Param.class);

    public static List<Param> all() {
        return find.all();
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
