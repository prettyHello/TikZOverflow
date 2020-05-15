package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.User;
import be.ac.ulb.infof307.g09.model.DALServices;
import be.ac.ulb.infof307.g09.model.DAO;
import org.springframework.security.crypto.bcrypt.BCrypt;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.File;

import static be.ac.ulb.infof307.g09.controller.ControllerUtility.checkObjects;

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
        createUserFolder(String.valueOf(userDb.getUserId()));
    }

    /**
     * Creates the folder that will contain the projects of a user
     *
     * @param userId the id of the user the projects will belong to
     */
    private void createUserFolder(String userId) throws FatalException {
        File projectDirectory = new File(rootProject);
        if(!projectDirectory.exists()){
            if(!projectDirectory.mkdir()){
                throw new FatalException("Unable to create PorjectTikz folder");
            }
        }
        File file = new File(rootProject + "userid_" + userId);
        if (!file.exists()) {
            if (!file.mkdir()) {
                throw new FatalException("Unable to create user folder");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserInfo(UserDTO userDTO) throws BizzException, FatalException {
        checkObjects(userDTO);
        userDAO.update(userDTO);
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
        userDAO.create(userDTO);
    }
}
