package persistence;

import business.DTO.UserDTO;
import business.factories.UserFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    private DALServices dalServices;
    private UserFactory userFactory;


    private static final String SQL_INSERT_USER = "INSERT INTO users(first_name, last_name, email, phone, password, salt, register_date ) VALUES (?, ?, ?, ?, ?, ?,?)";
    private static final String SQL_LOGIN_USER = "SELECT * FROM user WHERE email=?";

    public UserDAOImpl(DALServices dalServices, UserFactory userFactory) {
        this.dalServices = dalServices;
        this.userFactory = userFactory;
    }

    @Override
    public UserDTO getUser(UserDTO usrAuth){
        PreparedStatement pr;
        ResultSet rs;
        UserDTO usr;
        try {
            pr = ((DALBackEndServices) this.dalServices).prepareStatement(SQL_LOGIN_USER);
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
                return usr;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDTO find(UserDTO obj) {
        return null;
    }

    @Override
    public int create(UserDTO userDTO) {
        int insertedID = 0;
        PreparedStatement ps = null;
        try {
            ps = ((DALBackEndServices) this.dalServices).prepareStatement(SQL_INSERT_USER);
            ps.setString(1, userDTO.getFirst_name());
            ps.setString(2, userDTO.getLast_name());
            ps.setString(3, userDTO.getEmail());
            ps.setString(4, userDTO.getPhone());
            ps.setString(5, userDTO.getPassword());
            ps.setString(6, userDTO.getSalt());
            ps.setString(7, userDTO.getRegister_date());
            ps.executeUpdate();

        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("error in the DAO create user");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException exc) {
                    exc.printStackTrace();
                }
            }
        }
        return insertedID;
    }

    @Override
    public Long update(UserDTO userDTO) {
        return null;
    }

    @Override
    public void delete(UserDTO userDTO) {

    }
}
