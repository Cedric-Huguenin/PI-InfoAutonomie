package controllers;

import model.Token;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by Ced on 06/03/2015.
 */
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get("AUTH");
        if(authTokenHeaderValues != null && authTokenHeaderValues.length == 1) {
            Token token = Token.find.where().eq("token", authTokenHeaderValues[0]).findUnique();
            if(token != null) {
                return token.getId();
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return forbidden();
    }
}