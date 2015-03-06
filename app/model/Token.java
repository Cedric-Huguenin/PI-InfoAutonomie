package model;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by Ced on 06/03/2015.
 */

@Entity
public class Token extends Model{

    @Id
    public String id;

    public String token;

    public static Model.Finder<String,Token> find = new Model.Finder<>(String.class, Token.class);

    public static List<Token> all() {
        return find.all();
    }

    public static Token create(Token token) {
        token.save();
        return token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
