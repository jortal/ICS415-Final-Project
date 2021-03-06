import models.UserInfoDB;
import play.Application;
import play.GlobalSettings;

/**
 * Provide initialization code for the digits application.
 * @author Philip Johnson
 */
public class Global extends GlobalSettings {

  /**
   * Initialize the system with some sample contacts.
   * @param app The application.
   */
  public void onStart(Application app) {
    System.out.println(UserInfoDB.getUsers());
    if (UserInfoDB.getUsers().isEmpty()) {
      UserInfoDB.addUserInfo("John Smith", "smith@example.com", "password");
      UserInfoDB.addUserInfo("Guest", "guest@example.com", "guest");
      UserInfoDB.addUserInfo("Administrator", "admin@example.com", "admin");
    }
  }
}
