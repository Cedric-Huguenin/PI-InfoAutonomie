package model;

import play.db.ebean.Model;
import utils.PasswordHash;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Account class
 */
@Entity
public class Account extends Model {

    @Id
    public String email;

    public String hashedPassword;

    public boolean isAdmin;

    public static Account create(Account account) {
        account.save();
        return account;
    }

    public static Model.Finder<String,Account> find = new Model.Finder<>(String.class, Account.class);

    public static List<Account> all() {
        return find.all();
    }

    public static Account authenticate(String email, String password) {
        String hashed = PasswordHash.hashed(password);
        return find.where().eq("email", email).eq("hashedPassword", hashed).findUnique();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
