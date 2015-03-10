package controllers;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

/**
 * Authorization
 */
public class Authorization extends Action.Simple {
    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        if(context.session().get("email") != null) {
            return delegate.call(context);
        }


        context.flash().put("warning","Veuillez vous connecter pour accéder à cette ressource");
        return F.Promise.pure(LoginController.redirectLogin());

    }
}
