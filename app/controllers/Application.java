package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.Index;
import views.html.Profile;
import views.html.Login;
import views.html.About;
import views.formdata.LoginFormData;
import play.mvc.Security;
import models.UserInfo;

/**
 * Implements the controllers for this application.
 */
public class Application extends Controller {

  /**
   * Provides the About page.
   * @return The About page. 
   */
  public static Result about() {
    UserInfo userInfo = Secured.getUserInfo(ctx());
    if (userInfo != null) {
      if (userInfo.getEmail() == "admin@example.com") {
        boolean bool = true;
        return ok(About.render("About", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), bool));        
      }
    }    
    return ok(About.render("About", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), false));
  }
  
  /**
   * Provides the Index page.
   * @return The Index page. 
   */
  public static Result index() {
    UserInfo userInfo = Secured.getUserInfo(ctx());
    if (userInfo != null) {
      if (userInfo.getEmail() == "admin@example.com") {
        boolean bool = true;
        return ok(Index.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), bool));        
      }
    }
    return ok(Index.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), false));
  }
  
  /**
   * Provides the Login page (only to unauthenticated users). 
   * @return The Login page. 
   */
  public static Result login() {
    Form<LoginFormData> formData = Form.form(LoginFormData.class);
    return ok(Login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), false, formData));
  }

  /**
   * Processes a login form submission from an unauthenticated user. 
   * First we bind the HTTP POST data to an instance of LoginFormData.
   * The binding process will invoke the LoginFormData.validate() method.
   * If errors are found, re-render the page, displaying the error data. 
   * If errors not found, render the page with the good data. 
   * @return The index page with the results of validation. 
   */
  public static Result postLogin() {

    // Get the submitted form data from the request object, and run validation.
    Form<LoginFormData> formData = Form.form(LoginFormData.class).bindFromRequest();

    if (formData.hasErrors()) {
      flash("error", "Login credentials not valid.");
      return badRequest(Login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), false, formData));
    }
    else {
      // email/password OK, so now we set the session variable and only go to authenticated pages.
      session().clear();
      session("email", formData.get().email);
      return redirect(routes.Application.index());
    }
  }
  
  /**
   * Logs out (only for authenticated users) and returns them to the Index page. 
   * @return A redirect to the Index page. 
   */
  @Security.Authenticated(Secured.class)
  public static Result logout() {
    session().clear();
    return redirect(routes.Application.index());
  }
  
  /**
   * Provides the Profile page (only to authenticated users).
   * @return The Profile page. 
   */
  @Security.Authenticated(Secured.class)
  public static Result profile() {
    UserInfo userInfo = Secured.getUserInfo(ctx());    
    if (userInfo.getEmail() == "admin@example.com") {
      boolean bool = true;    
      return ok(Profile.render("Profile", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), bool));
    }
    return ok(Profile.render("Profile", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), false));    
  }
}
