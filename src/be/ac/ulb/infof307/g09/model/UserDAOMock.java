package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;

public class UserDAOMock implements UserDAO {

    private final UserFactory userFactory;

    public UserDAOMock(DALServices dalServices, UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @Override
    public UserDTO create(UserDTO obj) {
        obj.setUserId(1);
        return obj;
    }

    @Override
    public void update(UserDTO obj) {
        //update returns void thus the mock doesn't have anything to mimic
    }

    @Override
    public void delete(UserDTO obj) {
        //delete returns void thus the mock doesn't have anything tasks to mimic
    }

    @Override
    public UserDTO get(UserDTO usrAuth) {
        return testWrongPassword();
    }

    private UserDTO testWrongPassword() {
        UserDTO user = userFactory.createUser();
        user.setFirstName("Michel");
        user.setSalt("salt");
        user.setPassword(BCrypt.hashpw("password", "salt"));
        user.setEmail("mail@mail.be");
        user.setLastName("ber");
        user.setPhone("123");
        user.setRegisterDate(LocalDateTime.now().toString());
        return user;
    }
}