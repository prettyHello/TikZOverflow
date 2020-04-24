package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.config.TestDAOConfigurationSingleton;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import be.ac.ulb.infof307.g09.utilities.exceptions.BizzException;
import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserDAOImplTest {

    DALServices dalServices;
    UserFactory userFactory;
    DAO<UserDTO> userDAO;


    @BeforeEach
    void setUp() {
        TestDAOConfigurationSingleton holder = TestDAOConfigurationSingleton.getInstance();
        this.dalServices = TestDAOConfigurationSingleton.getDalServices();
        this.userFactory = TestDAOConfigurationSingleton.getUserFactory();
        this.userDAO = TestDAOConfigurationSingleton.getUserDAO();
        try {
            dalServices.createTables("dao_test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        dalServices.deleteDB("dao_test");
    }

    @Test
    void create_expectedBehaviour() {
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        UserDTO result = userDAO.get(user);
        assertEquals(user.getFirstName(), result.getFirstName(), "First name does not match");
        assertEquals(user.getEmail(), result.getEmail(), "Email does not match");
        assertEquals(user.getPassword(), user.getPassword(), "Password does not match");
        assertEquals(user.getPhone(), user.getPhone(), "Phone does not match");
        assertEquals(user.getLastName(), user.getLastName(), "LastName does not match");
        assertEquals(user.getRegisterDate(), user.getRegisterDate(), "Date does not match");
        assertEquals(user.getSalt(), user.getSalt(), "Salt does not match");
    }

    @Test
    void create_emailUniqueness() {
        UserDTO user = generateBasicUserDTO();
        UserDTO user2 = generateBasicUser2DTO();
        userDAO.create(user);
        assertThrows(FatalException.class, () -> {
            user2.setEmail("mail@mail.be");
            userDAO.create(user2);
        });
    }

    @Test
    void create_saltUniqueness() {
        UserDTO user = generateBasicUserDTO();
        UserDTO user2 = generateBasicUser2DTO();
        userDAO.create(user);
        assertThrows(FatalException.class, () -> {
            user2.setSalt("salt");
            userDAO.create(user2);
        });

    }

    @Test
    void create_phoneUniqueness() {
        UserDTO user = generateBasicUserDTO();
        UserDTO user2 = generateBasicUser2DTO();
        userDAO.create(user);
        assertThrows(FatalException.class, () -> {
            user2.setPhone("123");
            userDAO.create(user2);
        });

    }


    @Test
    void get_expectedBehaviour() {
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        UserDTO result = userDAO.get(user);
        assertEquals(user.getFirstName(), result.getFirstName(), "First name does not match");
        assertEquals(user.getEmail(), result.getEmail(), "Email does not match");
        assertEquals(user.getPassword(), user.getPassword(), "Password does not match");
        assertEquals(user.getPhone(), user.getPhone(), "Phone does not match");
        assertEquals(user.getLastName(), user.getLastName(), "LastName does not match");
        assertEquals(user.getRegisterDate(), user.getRegisterDate(), "Date does not match");
        assertEquals(user.getSalt(), user.getSalt(), "Salt does not match");
    }

    @Test
    void get_noUser() {
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        assertThrows(BizzException.class, () -> {
            user.setEmail("oops");
            userDAO.get(user);
        });
    }

    @Test
    void update_phoneUniqueness() {
        //  error same phone number
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        UserDTO user2 = generateBasicUser2DTO();
        userDAO.create(user2);
        assertThrows(FatalException.class, () -> {
            user.setPhone("049");
            userDAO.update(user);
        }, "update phone number to an existing one doesn't raise an exception");

    }

    @Test
    void update_mailUniqueness() {
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        UserDTO user2 = generateBasicUser2DTO();
        userDAO.create(user2);

        // error same email
        assertThrows(FatalException.class, () -> {
            user.setEmail("mail2@mail.be");
            userDAO.update(user);
        }, "update email to an existing one doesn't raise an exception");
    }

    @Test
    void update_saltUniqueness() {
        // error same salt
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        UserDTO user2 = generateBasicUser2DTO();
        userDAO.create(user2);
        assertThrows(FatalException.class, () -> {
            user.setSalt("salt2");
            userDAO.update(user);
        }, "update salt to an existing one doesn't raise an exception");
    }

    @Test
    void delete_expectedBehaviour() {
        // test 1: create then delete then see if still exists
        UserDTO user = generateBasicUserDTO();
        assertThrows(BizzException.class, () -> {
            userDAO.create(user);
            userDAO.delete(user);
            userDAO.get(user);
        }, "getUser still return a user after delete was called");
    }

    private UserDTO generateBasicUserDTO() {
        UserDTO user = userFactory.createUser();
        user.setFirstName("ben");
        user.setPassword("pass");
        user.setSalt("salt");
        user.setEmail("mail@mail.be");
        user.setLastName("ber");
        user.setPhone("123");
        user.setRegisterDate(LocalDateTime.now().toString());
        return user;
    }

    private UserDTO generateBasicUser2DTO() {
        UserDTO user2 = userFactory.createUser();
        user2.setFirstName("mat");
        user2.setPassword("pass2");
        user2.setSalt("salt2");
        user2.setEmail("mail2@mail.be");
        user2.setLastName("han");
        user2.setPhone("049");
        user2.setRegisterDate(LocalDateTime.now().toString());
        return user2;
    }
}