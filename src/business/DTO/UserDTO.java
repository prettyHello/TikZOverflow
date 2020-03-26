package business.DTO;

/**
 * The user DTO contains all the data related to the user and is serializable
 * the user DTO travels between the mvc layers of the application
 */
public interface UserDTO {

    String getLast_name();

    String getEmail();

    String getPhone();

    String getPassword();

    String getSalt();

    String getRegister_date();

    int getUser_id();

    void setUser_id(int user_id);

    void setFirst_name(String first_name);

    void setLast_name(String last_name);

    void setEmail(String email);

    void setPhone(String phone);

    void setPassword(String password);

    void setSalt(String salt);

    void setRegister_date(String register_date);

    void setAuthorized(boolean authorized);

    boolean isAuthorized();

    String getFirst_name();
}
