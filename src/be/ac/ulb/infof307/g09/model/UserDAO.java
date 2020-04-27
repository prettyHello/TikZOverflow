package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;

/**
 * The UserDAO extends DAO (basic CRUD operations) and can be used to add specific methods related to Users
 * Such as find user by email.
 *
 * Even if it's empty for now, it needs to already be in place, just in order to be constant with other dao
 * like ProjectDAO who already implement new methods.
 */
public interface UserDAO extends DAO<UserDTO> {

}
