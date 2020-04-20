package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.User;
import be.ac.ulb.infof307.g09.model.DALServices;
import be.ac.ulb.infof307.g09.model.DAO;
import org.springframework.security.crypto.bcrypt.BCrypt;
import be.ac.ulb.infof307.g09.utilities.exceptions.BizzException;
import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

import java.io.File;

import static be.ac.ulb.infof307.g09.utilities.Utility.checkObjects;

/**
 * {@inheritDoc}
 */
public class UserUCCImpl implements UserUCC {

    private final DALServices dal;
    private final DAO<UserDTO> userDAO;
    private final String rootProject = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator;

    public UserUCCImpl(DALServices dalServices, DAO<UserDTO> userDAO) {
        this.dal = dalServices;
        this.userDAO = userDAO;
    }

    /**
     * {@inheritDoc}
     */
    public void login(UserDTO user) throws BizzException, FatalException {
        checkObjects(user);
        User userDb = (User) userDAO.get(user);
        /* The salt is incorporated into the hash (encoded in a base64-style format).
         * That's why the salt shouldn't actually be saved */
        if (userDb == null || !BCrypt.checkpw(user.getPassword(), userDb.getPassword())) {
            throw new BizzException("Wrong Password");
        }
        ConnectedUser.setConnectedUser(userDb);
        //TODO Move dans utility
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
        checkObjects(userDTO);
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
        checkObjects(user);
        return userDAO.get(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(UserDTO userDTO) throws FatalException {
        checkObjects(userDTO);
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
