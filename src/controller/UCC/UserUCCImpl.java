package controller.UCC;

import controller.DTO.UserDTO;
import controller.User;
import model.DALServices;
import model.DAO;
import org.springframework.security.crypto.bcrypt.BCrypt;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.File;

/**
 * {@inheritDoc}
 */
public class UserUCCImpl implements UserUCC {

    private final DALServices dal;
    private final DAO<UserDTO> userDAO;
    private String rootProject = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator;

    public UserUCCImpl(DALServices dalServices, DAO<UserDTO> userDAO) {
        this.dal = dalServices;
        this.userDAO = userDAO;
    }

    /**
     * {@inheritDoc}
     */
    //TODO file in dao please
    public void login(UserDTO user) throws BizzException, FatalException {
        utilities.Utility.checkObjects(user);
        User userDb = (User) userDAO.get(user);
        /* The salt is incorporated into the hash (encoded in a base64-style format).
         * That's why the salt shouldn't actually be saved */
        if (userDb == null || !BCrypt.checkpw(user.getPassword(), userDb.getPassword())) {
            throw new BizzException("Wrong Password");
        }
        ConnectedUser.setConnectedUser(userDb);
        File file = new File(rootProject +"userid_" +userDb.getUserId());
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserInfo(UserDTO userDTO) throws BizzException, FatalException {
        //TODO need to check userDTO with utils
        //good implementation, a little bit empty
        utilities.Utility.checkObjects(userDTO);
        try {
            dal.startTransaction();
            //TODO add business constrains here or remove the transaction/rollback/commit
            userDAO.update(userDTO);
            dal.commit();
        } finally {
            dal.rollback();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnectedUser(UserDTO user) {
        ConnectedUser.setConnectedUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteConnectedUser() {
        ConnectedUser.deleteConnectedUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getConnectedUser() {
        return ConnectedUser.getConnectedUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserInfo(UserDTO user) throws BizzException, FatalException {
        utilities.Utility.checkObjects(user);
        return userDAO.get(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(UserDTO userDTO) throws FatalException {
        utilities.Utility.checkObjects(userDTO);
        try {
            dal.startTransaction();
            //TODO add business constrains here or remove the transaction/rollback/commit
            userDAO.create(userDTO);
            dal.commit();
        } finally {
            dal.rollback();
        }
    }
}
