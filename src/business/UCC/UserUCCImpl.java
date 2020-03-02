package business.UCC;

import business.DTO.UserDTO;
import org.springframework.security.crypto.bcrypt.BCrypt;
import persistence.DALServices;
import persistence.DAO;

import java.util.HashMap;
import java.util.Map;

import business.User;

public class UserUCCImpl implements UserUCC {

    private final DALServices dalServices;
    private final DAO<UserDTO> userDAO;

    public UserUCCImpl(DALServices dalServices, DAO<UserDTO> userDAO) {
        this.dalServices = dalServices;
        this.userDAO = userDAO;
    }


    public boolean login(UserDTO user) {
        User usr = (User) user;
        try {
            this.dalServices.startTransaction();
            User userDb = (User) userDAO.getUser(user);

            //TODO check password immediately ? 'Real' password management with security story ?

            /*
            * The salt is incorporated into the hash (encoded in a base64-style format).
            * That's why the salt shouldn't actually be saved */
            if (userDb == null || !BCrypt.checkpw(user.getPassword(), userDb.getPassword())) {
                Map<String, Object> errorsLog = new HashMap<String, Object>();
                errorsLog.put("password", "Le pseudo et le mot de passe ne correspondent pas.");
                throw new Exception();
            }
            this.dalServices.commit();

        } catch (Exception e) {
            this.dalServices.rollback();
            return false;
        }
        return true;
    }

    public boolean updateUserInfo(UserDTO userDTO) {
        boolean success = false;
        try {
            this.dalServices.startTransaction();
            success = userDAO.updateUser(userDTO);
            this.dalServices.commit();
        } catch (Exception e) {
            this.dalServices.rollback();
            return false;
        }
        return success;
        //TODO really bad implementation, need to add exceptions and not use return null,will change it later, probably.
    }

    public UserDTO getUserInfo(UserDTO user){
        User usr = (User) user;
        User userDb = null;
        try {
            this.dalServices.startTransaction();
            userDb = (User) userDAO.getUser(user);
            this.dalServices.commit();
        } catch (Exception e) {
            this.dalServices.rollback();
        }
        return (UserDTO) userDb;
        //TODO really bad implementation, need to add exceptions and not use return null,will change it later, probably.
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        //add to DTO/UserImpl a method that check if fields are valid (or do that in javafx, as u wish)
        dalServices.startTransaction(); //we could start the transaction later since it's desktop, but if it was a website we would nee to do it before the checks to avoid race conditions
            //check if email already exists
            //userDTO.setPassword(       hash(userDTO.getPassword())         );
            int id = userDAO.create(userDTO);
            // if error
                // dalServices.rollback();
            //else
                dalServices.commit();
        userDTO.setUser_id(id);
        userDTO.setAuthorized(true);

        return  userDTO;
    }
}
