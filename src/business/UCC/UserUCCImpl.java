package business.UCC;

import business.DTO.UserDTO;
import exceptions.BizzException;
import exceptions.FatalException;
import org.springframework.security.crypto.bcrypt.BCrypt;


import persistence.DALServices;
import persistence.DAO;

import business.User;

public class UserUCCImpl implements UserUCC {

    private final DALServices dal;
    private final DAO<UserDTO> userDAO;

    public UserUCCImpl(DALServices dalServices, DAO<UserDTO> userDAO) {
        this.dal = dalServices;
        this.userDAO = userDAO;
    }


    public void login(UserDTO user) throws BizzException, FatalException {
        utilities.Utility.checkObject(user);
        User usr = (User) user;
        try {
            dal.startTransaction();
            User userDb = (User) userDAO.getUser(user);
            /*
             * The salt is incorporated into the hash (encoded in a base64-style format).
             * That's why the salt shouldn't actually be saved */
            if (userDb == null || !BCrypt.checkpw(user.getPassword(), userDb.getPassword())) {
                //dal.rollback();
                throw new BizzException("Wrong Password");
            }
            dal.commit();
            UserDTO connectedUser = getUserInfo(userDb);
            ConnectedUser.setConnectedUser(connectedUser);
        } finally {
            dal.rollback();
        }
    }

    public void updateUserInfo(UserDTO userDTO) throws BizzException, FatalException {
        //TODO need to check userDTO with utils
        //good implementation, a little bit empty
        utilities.Utility.checkObject(userDTO);
        try {
            dal.startTransaction();
            userDAO.update(userDTO);
            dal.commit();
        } finally {
            dal.rollback();
        }
    }

    @Override
    public void setConnectedUser(UserDTO user) {
        ConnectedUser.setConnectedUser(user);
    }

    @Override
    public void deleteConnectedUser() {
        ConnectedUser.deleteConnectedUser();
    }

    @Override
    public UserDTO getConnectedUser() {
        return ConnectedUser.getConnectedUser();
    }

    public UserDTO getUserInfo(UserDTO user) throws BizzException, FatalException {
        utilities.Utility.checkObject(user);
        User usr = (User) user;
        User userDb = null;
        try {
            dal.startTransaction();
            userDb = (User) userDAO.getUser(user);
            dal.commit();
        } finally {
            dal.rollback();
        }
        return (UserDTO) userDb;
    }

    @Override
    public void register(UserDTO userDTO) throws BizzException, FatalException {
        utilities.Utility.checkObject(userDTO);
        try {
            dal.startTransaction();
            userDAO.create(userDTO);
            dal.commit();
        } finally {
            dal.rollback();
        }
    }
}
