package views.formdata;

import java.util.ArrayList;
import java.util.List;
import models.UserInfo;
import models.UserInfoDB;
import play.data.validation.ValidationError;

/**
 * 
 * Backing class for form data.
 * 
 */

public class UserFormData {
  
  /** Name form field. **/
  public String name = ""; 
  /** E-mail form field. **/
  public String email = ""; 
  /** Password form field. **/
  public String password = ""; 
  
  /** No argument constructor. **/
  public UserFormData() {
    // no arg constructor
  }
      
  /**
   * Constructs a UserFormData object manually for the purpose of initializing the contact table.
   */
  public UserFormData(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  /**
   * Constructor for the contact list.
   * @param contact The list of contacts.
   */
  
  public UserFormData(UserInfo user) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.password = user.getPassword();
  }

  /**
   * Validates form input by the user.
   * All fields must not be empty.
   * Telephone must be 12 characters.
   * @return null if no errors, list of ValidationErrors if there are errors.
   */
  public List<ValidationError> validate() {
    List<ValidationError> errors = new ArrayList<>();

    if (name == null || name.length() == 0) {
      errors.add(new ValidationError("name", "Name URL is required."));
    }    

    if (password == null || password.length() == 0) {
      errors.add(new ValidationError("password", "Password is required."));
    }        
    
    if (email == null || email.length() == 0) {
      errors.add(new ValidationError("email", "Email is required."));
    }        
        
    if (UserInfoDB.isUser(email) == true) {
      System.out.println(UserInfoDB.isUser(email));
      errors.add(new ValidationError("email", "Email exists, use another"));      
    }
    
    return errors.isEmpty() ? null : errors;
  }

}