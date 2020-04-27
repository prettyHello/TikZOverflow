package be.ac.ulb.infof307.g09.controller;

public class UserImpl implements User {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String salt;
    private String registerDate;
    private boolean isAuthorized;

    public UserImpl() {
    }

    public UserImpl(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }

    public UserImpl(int userId, String firstName, String lastName, String email, String phone, String password, String salt, String registerDate) {
        this(email, password);
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.salt = salt;
        this.registerDate = registerDate;
    }


    //---------------- getters and setters ---------------//
    public String getLastName() {
        return lastName;
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

    public String getRegisterDate() {
        return registerDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }
}
