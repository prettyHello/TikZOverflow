package persistence;

import business.DTO.UserDTO;
import business.factories.UserFactory;
import exceptions.BizzException;
import exceptions.FatalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.DAOConfigurationSingleton;

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
        DAOConfigurationSingleton holder = DAOConfigurationSingleton.getInstance();
        this.dalServices = DAOConfigurationSingleton.getDalServices();
        this.userFactory = DAOConfigurationSingleton.getUserFactory();
        this.userDAO = DAOConfigurationSingleton.getUserDAO();
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
    void create() {

        // Test1: simple, working insert
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        UserDTO result = userDAO.getUser(user);

        assertEquals(user.getFirst_name(), result.getFirst_name(), "First name does not match");
        assertEquals(user.getEmail(), result.getEmail(), "Email does not match");
        assertEquals(user.getPassword(), user.getPassword(), "Password does not match");
        assertEquals(user.getPhone(), user.getPhone(), "Phone does not match");
        assertEquals(user.getLast_name(), user.getLast_name(), "LastName does not match");
        assertEquals(user.getRegister_date(), user.getRegister_date(), "Date does not match");
        assertEquals(user.getSalt(), user.getSalt(), "Salt does not match");

        // Test2: error same email
        UserDTO user2 = generateBasicUser2DTO();

        assertThrows(FatalException.class, () -> {
            user2.setEmail("mail@mail.be");
            userDAO.create(user2);
        });

        // Test3: error same salt
        assertThrows(FatalException.class, () -> {
            user2.setSalt("salt");
            userDAO.create(user2);
        });

        // Test4: error same phone number
        assertThrows(FatalException.class, () -> {
            user2.setPhone("123");
            userDAO.create(user2);
        });


    }

    @Test
    void getUser() {
        // Test 1 User exist
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        UserDTO result = userDAO.getUser(user);
        assertEquals(user.getFirst_name(), result.getFirst_name(), "First name does not match");
        assertEquals(user.getEmail(), result.getEmail(), "Email does not match");
        assertEquals(user.getPassword(), user.getPassword(), "Password does not match");
        assertEquals(user.getPhone(), user.getPhone(), "Phone does not match");
        assertEquals(user.getLast_name(), user.getLast_name(), "LastName does not match");
        assertEquals(user.getRegister_date(), user.getRegister_date(), "Date does not match");
        assertEquals(user.getSalt(), user.getSalt(), "Salt does not match");

        // Test 2 User don't exist
        assertThrows(BizzException.class, () -> {
            user.setEmail("oops");
            userDAO.getUser(user);
        });
    }

    @Test
    void find() {
    }

    @Test
    void update() {
        UserDTO user = generateBasicUserDTO();
        userDAO.create(user);
        UserDTO user2 = generateBasicUser2DTO();
        userDAO.create(user2);

        // Test2: error same email
        assertThrows(FatalException.class, () -> {
            user.setEmail("mail2@mail.be");
            userDAO.update(user);
        }, "update email to an existing one doesn't raise an exception");

        // Test3: error same salt
        assertThrows(FatalException.class, () -> {
            user.setSalt("salt2");
            userDAO.update(user);
        }, "update salt to an existing one doesn't raise an exception");
        // Test4: error same phone number

        assertThrows(FatalException.class, () -> {
            user.setPhone("123");
            userDAO.update(user);
        }, "update phone number to an existing one doesn't raise an exception");

    }


    @Test
    void delete() {
        // test 1: create then delete then see if still exists
        UserDTO user = generateBasicUserDTO();
        assertThrows(BizzException.class, () -> {
            userDAO.create(user);
            userDAO.delete(user);
            userDAO.getUser(user);
        }, "getUser still return a user after delete was called");

        // test 2: throw fatal exception if not exists
        assertThrows(FatalException.class, () -> {
            userDAO.delete(user);
        }, "delete doesn't throw a fatal exception when trying to delete a user that doesn't exists");

    }


    private UserDTO generateBasicUserDTO() {
        UserDTO user = userFactory.createUser();
        user.setFirst_name("ben");
        user.setPassword("pass");
        user.setSalt("salt");
        user.setEmail("mail@mail.be");
        user.setLast_name("ber");
        user.setPhone("123");
        user.setRegister_date(LocalDateTime.now().toString());
        return user;
    }

    private UserDTO generateBasicUser2DTO() {
        UserDTO user2 = userFactory.createUser();
        user2.setFirst_name("mat");
        user2.setPassword("pass2");
        user2.setSalt("salt2");
        user2.setEmail("mail2@mail.be");
        user2.setLast_name("han");
        user2.setPhone("049");
        user2.setRegister_date(LocalDateTime.now().toString());
        return user2;
    }

}