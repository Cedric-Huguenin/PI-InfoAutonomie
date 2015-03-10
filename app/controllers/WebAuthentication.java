package controllers;

import model.Account;
import model.Token;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Secured class
 *
 * Allow to verify if user is logged or not
 */
public class WebAuthentication extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context context) {
        if(context.session().get("email") != null) {
            return context.session().get("email");
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return forbidden();
    }
}
