package be.ac.ulb.infof307.g09.controller.DTO;

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

    void setUserId(int userId);

    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setEmail(String email);

    void setPhone(String phone);

    void setPassword(String password);

    void setSalt(String salt);

    void setRegisterDate(String registerDate);

    void setAuthorized(boolean authorized);

    boolean isAuthorized();

    String getFirstName();
}
