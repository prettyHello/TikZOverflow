package be.ac.ulb.infof307.g09.controller.factories;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.UserImpl;

/**
 * {@inheritDoc}
 */
public class UserFactoryImpl implements UserFactory {

    @Override
    public UserDTO createUser() {
        return new UserImpl();
    }

    public UserDTO createUser(String login, String password) {
        return new UserImpl(login, password);
    }

    public UserDTO createUser(int userId, String firstName, String lastName, String email, String phone, String password, String salt, String registerDate) {
        return new UserImpl(userId, firstName, lastName, email, phone, password, salt, registerDate);
    }
}
