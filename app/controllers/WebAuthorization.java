package controllers;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Authorization class
 *
 * Allow to personalize access to pages according to their rights
 */
public class WebAuthorization extends Action.Simple {
    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        if(context.session().get("email") != null && "true".equals(context.session().get("admin"))) {
            return delegate.call(context);
        }

        context.flash().put("warning","Vous ne disposez pas des droits suffisants pour accéder à cette ressource");
        return F.Promise.pure(LoginController.redirectLogin());

    }
}
