package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

/**
 * Controller that handles operations on users
 */
public interface UserUCC {

    void setConnectedUser(UserDTO user);

    void deleteConnectedUser();

    UserDTO getConnectedUser();

    /**
     * Returns the full information DTO for a specified user
     *
     * @param user the user to get info about
     * @return the full DTO
     * @throws BizzException  if the user doesn't exist
     * @throws FatalException if interaction with the database failed
     */
    UserDTO getUserInfo(UserDTO user) throws BizzException, FatalException;

    /**
     * Used to log a user in to the application
     *
     * @param userDTO the user to login with username and password
     * @throws BizzException  if the password is wrong
     * @throws FatalException if interaction with the database failed
     */
    void login(UserDTO userDTO) throws BizzException, FatalException;

    /**
     * Used to register a new user to the application
     *
     * @param userDTO the user to register
     * @throws FatalException if interaction with the database failed
     */
    void register(UserDTO userDTO) throws FatalException;

    /**
     * Used to update the saved data of a user
     *
     * @param userDTO the new info of the user
     * @throws BizzException
     * @throws FatalException if interaction with the database failed
     */
    void updateUserInfo(UserDTO userDTO) throws BizzException, FatalException;
}
