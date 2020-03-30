package controller.DTO;

/**
 * The user DTO contains all the data related to the user and is serializable
 * the user DTO travels between the mvc layers of the application
 */
public interface UserDTO {

    String getLastName();

    String getEmail();

    String getPhone();

    String getPassword();

    String getSalt();

    String getRegisterDate();

    int getUserId();

    void setUserId(int user_id);

    void setFirstName(String first_name);

    void setLastName(String last_name);

    void setEmail(String email);

    void setPhone(String phone);

    void setPassword(String password);

    void setSalt(String salt);

    void setRegisterDate(String register_date);

    void setAuthorized(boolean authorized);

    boolean isAuthorized();

    String getFirstName();
}
