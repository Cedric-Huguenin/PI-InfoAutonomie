package controllers;

import model.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import views.html.param.params;
import views.html.param.editParam;

import java.util.List;
import java.util.Locale;

import static play.data.Form.form;

/**
 * Controller to handle and display BasicEvent objects.
 * Created by Mathieu on 07/02/2015.
 */
@Security.Authenticated(WebAuthentication.class)
public class ParamController extends Controller {

    @With(WebAuthorization.class)
    public static Result params() {

        return ok(params.render(Param.all()));
    }

    @With(WebAuthorization.class)
    public static Result edit(String id) {
        Form<Param> paramForm = Form.form(Param.class);
        Param param = Param.find.ref(id);

        paramForm.data().put("paramKey", param.getParamKey());
        paramForm.data().put("paramValue", param.getParamValue());

        return ok(editParam.render(paramForm));
    }
//
    @With(WebAuthorization.class)
    public static Result save() {
        Form<Param> paramForm = form(Param.class).bindFromRequest();

        Param param = Param.find.ref(paramForm.data().get("paramKey"));
        param.setParamValue(paramForm.data().get("paramValue"));
        param.update();

        return redirect(controllers.routes.ParamController.params());
    }

}
