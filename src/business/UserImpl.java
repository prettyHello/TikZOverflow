package business;

public class UserImpl implements User {
    private int user_id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String password;
    private String salt;
    private String register_date;
    private boolean isAuthorized; //this is not in the db

    public UserImpl() {
    }

    public UserImpl(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }

    //Full constructor
    public UserImpl(int user_id, String first_name, String last_name, String email, String phone, String password, String salt, String register_date) {
        this(email, password);
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.salt = salt;
        this.register_date = register_date;
    }



    //---------------- getters and setters ---------------//
    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getRegister_date() {
        return register_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }


}
