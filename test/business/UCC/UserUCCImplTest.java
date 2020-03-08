package business.UCC;

import business.DTO.UserDTO;
import business.factories.UserFactory;
import persistence.DALServices;
import persistence.DAO;
import utilities.BusinessConfigurationSingleton;


class UserUCCImplTest {
    DALServices dalServices;
    UserFactory userFactory;
    DAO<UserDTO> userDAO;
    //UserUCC userUcc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        this.dalServices = BusinessConfigurationSingleton.getDalServices();
        this.userDAO = BusinessConfigurationSingleton.getUserDAO();
        this.userFactory = BusinessConfigurationSingleton.getUserFactory();
        //this.userUcc = BusinessTestConfigurationHolder.getUserUcc();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void login() {

    }

    @org.junit.jupiter.api.Test
    void updateUserInfo() {
    }

    @org.junit.jupiter.api.Test
    void getUserInfo() {
    }

    @org.junit.jupiter.api.Test
    void register() {
    }
}