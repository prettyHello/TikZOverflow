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

    public UserDAOImpl(DALServices dalServices, UserFactory userFactory) {
        this.dalServices = dalServices;
        this.userFactory = userFactory;
    }

    @Override
    public UserDTO find(UserDTO userDTO) {
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
