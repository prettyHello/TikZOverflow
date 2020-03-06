package persistence;

import business.DTO.UserDTO;
import business.factories.UserFactory;
import business.factories.UserFactoryImpl;
import exceptions.FatalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.sqlite.SQLiteException;
import utilities.DAOTestConfigurationHolder;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@ContextConfiguration(locations = "classpath:application-context-test.xml")
class UserDAOImplTest {

    DALServices dalServices;
    UserFactory userFactory;
    DAO<UserDTO> userDAO;


    @BeforeEach
    void setUp() {
        /*this.dalServices =  DAOTestConfigurationHolder.getDalServices();
        this.userFactory = DAOTestConfigurationHolder.getUserFactory();
        this.userDAO = DAOTestConfigurationHolder.getUserDAO();*/
        dalServices = new DALServicesImpl();
        userFactory = new UserFactoryImpl();
        userDAO = new UserDAOImpl(dalServices, userFactory);
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
    @Rollback
    void create() {
/*
        // Test1: simple, working insert
        UserDTO user = userFactory.createUser();
        user.setFirst_name("ben");
        user.setPassword("pass");
        user.setSalt("salt");
        user.setEmail("mail@mail.be");
        user.setLast_name("ber");
        user.setPhone("0032");
        user.setRegister_date(LocalDateTime.now().toString());

        userDAO.create(user);
        UserDTO result = userDAO.getUser(user);

        assertEquals(user.getFirst_name(), result.getFirst_name(), "First name does not match");
        assertEquals(user.getEmail(), result.getEmail(), "Email does not match");
        assertEquals(user.getPassword(),user.getPassword(), "Password does not match");
        assertEquals(user.getPhone(),user.getPhone(), "Phone does not match");
        assertEquals(user.getLast_name(), user.getLast_name(), "LastName does not match");
        assertEquals(user.getRegister_date(), user.getRegister_date(), "Date does not match");
        assertEquals(user.getSalt(), user.getSalt(), "Salt does not match");

        // Test2: error same email
        UserDTO user2 = userFactory.createUser();
        user2.setFirst_name("mat");
        user2.setPassword("pass2");
        user2.setSalt("salt2");
        user2.setEmail("mail@mail.be");
        user2.setLast_name("han");
        user2.setPhone("049");
        user2.setRegister_date(LocalDateTime.now().toString());

        assertThrows(FatalException.class, () -> {
            userDAO.create(user2);
        });

        // Test3: error same salt
        user2.setFirst_name("mat");
        user2.setPassword("pass2");
        user2.setSalt("salt");
        user2.setEmail("mail2@mail.be");
        user2.setLast_name("han");
        user2.setPhone("049");
        user2.setRegister_date(LocalDateTime.now().toString());

        assertThrows(FatalException.class, () -> {
            userDAO.create(user2);
        });

        // Test4: error same phone number
        user2.setFirst_name("mat");
        user2.setPassword("pass2");
        user2.setSalt("salt2");
        user2.setEmail("mail2@mail.be");
        user2.setLast_name("han");
        user2.setPhone("0032");
        user2.setRegister_date(LocalDateTime.now().toString());

        assertThrows(FatalException.class, () -> {
            userDAO.create(user2);
        });

*/
    }

    @Test
    @Rollback
    void getUser() {
    }

    @Test
    @Rollback
    void find() {
    }

    @Test
    @Rollback
    void update() {
        UserDTO user = userFactory.createUser();
        user.setFirst_name("ben");
        user.setPassword("pass");
        user.setSalt("salt");
        user.setEmail("mail@mail.be");
        user.setLast_name("ber");
        user.setPhone("123");
        user.setRegister_date(LocalDateTime.now().toString());
        userDAO.create(user);

        UserDTO user2 = userFactory.createUser();
        user2.setFirst_name("ben");
        user2.setPassword("pass");
        user2.setSalt("salt2");
        user2.setEmail("mail2@mail.be");
        user2.setLast_name("ber");
        user2.setPhone("1234");
        user2.setRegister_date(LocalDateTime.now().toString());
        userDAO.create(user2);

        // Test2: error same email
        assertThrows(FatalException.class, () -> {
            user.setEmail("mail2@mail.be");

            userDAO.update(user);
        });

        // Test3: error same salt
        assertThrows(FatalException.class, () -> {
            user.setSalt("salt2");

            userDAO.update(user);
        });
        // Test4: error same phone number

        assertThrows(FatalException.class, () -> {
            user.setPhone("123");
            userDAO.update(user);
        },"test 4 update fail");

    }

    @Test
    @Rollback
    void delete() {
    }
}