package persistence;

import business.DTO.UserDTO;

/**
 * The UserDAO extends DAO (basic CRUD operations) and can be used to add specific methods related to Users
 * Such as find user by email
 */
public interface UserDAO extends DAO<UserDTO> {

}
