package model;

/**
 * Login class
 */
public class Login {

    public String email;
    public String password;


    public String validate() {
        if (Account.authenticate(email, password) == null) {
            return "Email ou mot de passe incorrect";
        }
        return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
