package controllers;

import model.Account;
import model.Login;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.login;

import static play.data.Form.form;

/**
 * Login controller
 */
public class LoginController extends Controller {

    public static Result login() {
        Form<Login> form = form(Login.class);
        return ok(login.render(form));
    }

    public static Result redirectLogin() {
        return redirect(controllers.routes.LoginController.login());
    }

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            Account account = Account.find.ref(loginForm.get().email);
            session("admin", account.isAdmin()+""); // check in DB
            return redirect(controllers.routes.Application.index());
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "Vous êtes déconnecté");
        return redirect(controllers.routes.LoginController.login());
    }

}
