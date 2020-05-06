package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static be.ac.ulb.infof307.g09.view.Utility.showAlert;

/**
 * {@inheritDoc}
 */
public class UserDAOImpl implements UserDAO {

    private static final String SQL_INSERT_USER = "INSERT INTO users(first_name, last_name, email, phone, password, salt, register_date ) VALUES (?, ?, ?, ?, ?, ?,?)";
    private static final String SQL_LOGIN_USER = "SELECT * FROM users WHERE email=?";
    private static final String SQL_UPDATE_USER = "UPDATE users SET first_name=?, last_name=?, email=?, phone=?, password=?, salt=?  WHERE email=?";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE email=?";

    private final DALBackEndServices dal;
    private final UserFactory userFactory;

    public UserDAOImpl(DALServices dalServices, UserFactory userFactory) {
        this.dal = (DALBackEndServices) dalServices;
        this.userFactory = userFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO get(UserDTO usrAuth) throws FatalException, BizzException {
        PreparedStatement pr;
        ResultSet rs;
        UserDTO usr = null;
        try {
            pr = dal.prepareStatement(SQL_LOGIN_USER);
            pr.setString(1, usrAuth.getEmail());
            rs = pr.executeQuery();
            usr = this.userFactory.createUser();
            if (rs.next()) {
                usr.setEmail(rs.getString("email"));
                usr.setFirstName(rs.getString("first_name"));
                usr.setLastName(rs.getString("last_name"));
                usr.setPassword(rs.getString("password"));
                usr.setPhone(rs.getString("phone"));
                usr.setSalt(rs.getString("salt"));
                usr.setRegisterDate(rs.getString("register_date"));
                usr.setUserId(rs.getInt("user_id"));
            } else {
                throw new BizzException("User : " + usrAuth.getEmail() + " does not exist");
            }
        } catch (SQLException exc) {
            showAlert(Alert.AlertType.WARNING, "DB Error", "DataBase Connection error", exc.getMessage());
            exc.printStackTrace();
            throw new FatalException("An error occurred in getUser");
        }
        return usr;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(UserDTO userDTO) throws FatalException {
        PreparedStatement ps = null;
        try {
            ps = dal.prepareStatement(SQL_INSERT_USER);
            ps.setString(1, userDTO.getFirstName());
            ps.setString(2, userDTO.getLastName());
            ps.setString(3, userDTO.getEmail());
            ps.setString(4, userDTO.getPhone());
            ps.setString(5, userDTO.getPassword());
            ps.setString(6, userDTO.getSalt());
            ps.setString(7, userDTO.getRegisterDate());
            ps.executeUpdate();
        } catch (SQLException exc) {
            if (exc.getErrorCode() == 19) {
                throw new FatalException("Email address or telephone number already in use.");
            }
            throw new FatalException("An error occurred in create of UserDAO");

        }
    }

    /**
     * Extracted method that fills a prepared statement with all the content of a UserDTO object
     *
     * @param ps      the SQL prepared statement that will need all the information of the DTO
     * @param userDTO the DTO containing the info of a user
     * @throws SQLException if an index is invalid for the statement
     */
    private void fullyFillPreparedStatement(PreparedStatement ps, UserDTO userDTO) throws SQLException {
        ps.setString(1, userDTO.getFirstName());
        ps.setString(2, userDTO.getLastName());
        ps.setString(3, userDTO.getEmail());
        ps.setString(4, userDTO.getPhone());
        ps.setString(5, userDTO.getPassword());
        ps.setString(6, userDTO.getSalt());
        ps.setString(7, userDTO.getRegisterDate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(UserDTO userDTO) throws FatalException {

        PreparedStatement ps = null;
        try {
            ps = dal.prepareStatement(SQL_UPDATE_USER);
            ps.setString(1, userDTO.getFirstName());
            ps.setString(2, userDTO.getLastName());
            ps.setString(3, userDTO.getEmail());
            ps.setString(4, userDTO.getPhone());
            ps.setString(5, userDTO.getPassword());
            ps.setString(6, userDTO.getSalt());
            ps.setString(7, userDTO.getEmail());
            ps.executeUpdate();
            System.out.println(ps);
        } catch (SQLException exc) {
            //exc.printStackTrace();
            throw new FatalException("An error occurred in updateUser");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UserDTO userDTO) {
        PreparedStatement ps = null;
        try {
            ps = dal.prepareStatement(SQL_DELETE_USER);
            ps.setString(1, userDTO.getEmail());
            ps.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
            throw new FatalException("An error occurred in deleteUSer");
        }
    }
}
