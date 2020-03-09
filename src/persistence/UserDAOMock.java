package persistence;

import business.DTO.UserDTO;
import business.factories.UserFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;

public class UserDAOMock implements UserDAO {

    private final DALBackEndServices dal;
    private final UserFactory userFactory;

    public UserDAOMock(DALServices dalServices, UserFactory userFactory) {
        this.dal = (DALBackEndServices) dalServices;
        this.userFactory = userFactory;
    }

    @Override
    public UserDTO find(UserDTO obj) {
        return null;
    }

    @Override
    public void create(UserDTO obj) {
    }

    @Override
    public void update(UserDTO obj) {
    }

    @Override
    public void delete(UserDTO obj) {

    }

    @Override
    public UserDTO getUser(UserDTO usrAuth) {
        return testWrongPassword();
    }

    private UserDTO testWrongPassword() {
        UserDTO user = userFactory.createUser();
        user.setFirst_name("Michel");
        user.setSalt("salt");
        user.setPassword(BCrypt.hashpw("password", "salt"));
        user.setEmail("mail@mail.be");
        user.setLast_name("ber");
        user.setPhone("123");
        user.setRegister_date(LocalDateTime.now().toString());
        return user;
    }
}