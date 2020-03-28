package persistence;

import business.DTO.UserDTO;
import business.factories.UserFactory;
import exceptions.BizzException;
import exceptions.FatalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@inheritDoc}
 */
public class UserDAOImpl implements UserDAO {
    private DALBackEndServices dal;
    private UserFactory userFactory;


    private static final String SQL_INSERT_USER = "INSERT INTO users(first_name, last_name, email, phone, password, salt, register_date ) VALUES (?, ?, ?, ?, ?, ?,?)";
    private static final String SQL_LOGIN_USER = "SELECT * FROM users WHERE email=?";
    private static final String SQL_UPDATE_USER = "UPDATE users SET first_name=?, last_name=?, email=?, phone=?, password=?, salt=?  WHERE email=?";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE email=?";

    public UserDAOImpl(DALServices dalServices, UserFactory userFactory) {
        this.dal = (DALBackEndServices) dalServices;
        this.userFactory = userFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUser(UserDTO usrAuth) throws FatalException, BizzException {
        PreparedStatement pr;
        ResultSet rs;
        UserDTO usr = null;
        try {
            pr = dal.prepareStatement(SQL_LOGIN_USER);
            pr.setString(1, usrAuth.getEmail());
            rs = pr.executeQuery();
            usr = (UserDTO) this.userFactory.createUser();
            if (rs.next()) {
                usr.setEmail(rs.getString("email"));
                usr.setFirst_name(rs.getString("first_name"));
                usr.setLast_name(rs.getString("last_name"));
                usr.setPassword(rs.getString("password"));
                usr.setPhone(rs.getString("phone"));
                usr.setSalt(rs.getString("salt"));
                usr.setRegister_date(rs.getString("register_date"));
                usr.setUser_id(rs.getInt("user_id"));
            } else {
                throw new BizzException("User : " + usrAuth.getEmail() + " does not exist");
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
            throw new FatalException("An error occurred in getUser");
        }
        return usr;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO find(UserDTO obj) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(UserDTO userDTO) throws FatalException {
        PreparedStatement ps = null;
        try {
            ps = dal.prepareStatement(SQL_INSERT_USER);
            ps.setString(1, userDTO.getFirst_name());
            ps.setString(2, userDTO.getLast_name());
            ps.setString(3, userDTO.getEmail());
            ps.setString(4, userDTO.getPhone());
            ps.setString(5, userDTO.getPassword());
            ps.setString(6, userDTO.getSalt());
            ps.setString(7, userDTO.getRegister_date());
            ps.executeUpdate();

        } catch (SQLException exc) {
            exc.printStackTrace();
            switch (exc.getErrorCode()) {
                case 19:
                    throw new FatalException("Email address or telephone number already in use.");
                default:
                    throw new FatalException("An error occurred in create of UserDAO");
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(UserDTO userDTO) throws FatalException {

        PreparedStatement ps = null;
        try {
            ps = dal.prepareStatement(SQL_UPDATE_USER);
            ps.setString(1, userDTO.getFirst_name());
            ps.setString(2, userDTO.getLast_name());
            ps.setString(3, userDTO.getEmail());
            ps.setString(4, userDTO.getPhone());
            ps.setString(5, userDTO.getPassword());
            ps.setString(6, userDTO.getSalt());
            ps.setString(7, userDTO.getEmail());
            ps.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
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
