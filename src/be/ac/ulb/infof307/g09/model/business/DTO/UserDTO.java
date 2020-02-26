package be.ac.ulb.infof307.g09.model.business.DTO;

/**
 * The user DTO contains all the data related to the user and is serializable
 * the user DTO travels between the mvc layers of the application
 */
public interface UserDTO {

    public String getLast_name();

    public String getEmail();

    public String getPhone();

    public String getPassword();

    public String getSalt();

    public String getRegister_date();

    public int getUser_id();

    public void setUser_id(int user_id);

    public void setFirst_name(String first_name);

    public void setLast_name(String last_name);

    public void setEmail(String email);

    public void setPhone(String phone);

    public void setPassword(String password);

    public void setSalt(String salt);

    public void setRegister_date(String register_date);

    public void setAuthorized(boolean authorized);

    public  boolean isAuthorized();

    public String getFirst_name();
}
