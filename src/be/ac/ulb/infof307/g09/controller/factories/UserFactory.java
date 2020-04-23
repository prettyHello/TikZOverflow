package be.ac.ulb.infof307.g09.controller.factories;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;

/**
 * This interface is meant to be used by the front end or the persistence by giving them the DTO they need
 */
public interface UserFactory {
    UserDTO createUser();

    UserDTO createUser(String login, String password);

    UserDTO createUser(int userId, String firstName, String lastName, String email, String phone, String password, String salt, String registerDate);
}
