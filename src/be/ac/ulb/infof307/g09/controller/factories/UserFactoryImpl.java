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

    public UserDTO createUser(int user_id, String first_name, String last_name, String email, String phone, String password, String salt, String register_date) {
        return new UserImpl(user_id, first_name, last_name, email, phone, password, salt, register_date);
    }
}
