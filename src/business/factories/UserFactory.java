package business.factories;

import business.DTO.UserDTO;

/**
 * This interface is meant to be used by the front end or the persistence by giving them the DTO they need
 */
public interface UserFactory {
    UserDTO createUser();

    UserDTO createUser(String login, String password);

    UserDTO createUser(int user_id, String first_name, String last_name, String email, String phone, String password, String salt, String register_date);
}
