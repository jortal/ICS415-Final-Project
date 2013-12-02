package controllers;

import java.util.Map;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.Index;
import views.html.NewUser;
import views.html.Users;
import views.html.Login;
import views.html.About;
import views.html.Features;
import views.formdata.LoginFormData;
import play.mvc.Security;
import models.UserInfo;
import models.UserInfoDB;
import views.formdata.UserFormData;

/**
 * Implements the controllers for this application.
 */
public class Application extends Controller {

  @Security.Authenticated(Secured.class)  
  public static Result newUser() {
    UserInfo userInfo = UserInfoDB.getUser(request().username());
    Boolean isLoggedIn = (userInfo != null);
    String user = userInfo.getEmail();
    UserFormData data = new UserFormData();
    Form<UserFormData> formData = Form.form(UserFormData.class).fill(data);
    if (isLoggedIn && userInfo.getEmail() != null) {
      return ok(NewUser.render("New", isLoggedIn, userInfo, true, formData));
    }
    else { return redirect(routes.Application.index()); }
  }

  @Security.Authenticated(Secured.class)  
  public static Result postUser() {
    Form<UserFormData> formData = Form.form(UserFormData.class).bindFromRequest();
    UserInfo userInfo = UserInfoDB.getUser(request().username());
    Boolean isLoggedIn = (userInfo != null);
    String user = userInfo.getEmail();
    if (formData.hasErrors()) {
      return badRequest(NewUser.render("New", isLoggedIn, userInfo, true, formData));
    }
    else {
      UserFormData data = formData.get();
      UserInfoDB.addUser(user, data);
      return redirect(routes.Application.index());
    }    
  }  

  /**
   * Provides the About page.
   * @return The About page. 
   */
  public static Result features() {
    UserInfo userInfo = Secured.getUserInfo(ctx());
    if (userInfo != null) {
      if (userInfo.getEmail().equals("admin@example.com")) {
        boolean bool = true;
        return ok(Features.render("Features", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), bool));        
      }
    }    
    return ok(Features.render("Features", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), false));
  }  
  
  /**
   * Provides the About page.
   * @return The About page. 
   */
  public static Result about() {
    UserInfo userInfo = Secured.getUserInfo(ctx());
    if (userInfo != null) {
      if (userInfo.getEmail().equals("admin@example.com")) {
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
      if (userInfo.getEmail().equals("admin@example.com")) {
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
   * Provides the Users page (only to authenticated users).
   * @return The Users page. 
   */
  @Security.Authenticated(Secured.class)
  public static Result users() {
    UserInfo userInfo = Secured.getUserInfo(ctx());    
    if (userInfo.getEmail().equals("admin@example.com")) {
      boolean bool = true;    
      return ok(Users.render("Users", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), bool,
                UserInfoDB.getUsers()));
    }
    return ok(Users.render("Users", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), false,
              UserInfoDB.getUsers()));    
  }

  /**
   * Deletes a user by e-mail.
   * @param email The email of the user to be deleted.
   */
  @Security.Authenticated(Secured.class)
  public static Result deleteUser(String email) {
    UserInfo userInfo = Secured.getUserInfo(ctx());    
    UserInfoDB.deleteUser(email);
    return redirect(routes.Application.index());
  }
  
}
