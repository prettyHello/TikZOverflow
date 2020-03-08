package business.UCC;

import business.DTO.UserDTO;
import business.factories.UserFactory;
import business.factories.UserFactoryImpl;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.DAO;
import persistence.UserDAOImpl;
import utilities.BusinessTestConfigurationHolder;


class UserUCCImplTest {
    DALServices dalServices;
    UserFactory userFactory;
    DAO<UserDTO> userDAO;
    //UserUCC userUcc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    /*   this.dalServices =  BusinessTestConfigurationHolder.getDalServices();
       this.userDAO = BusinessTestConfigurationHolder.getUserDAO();
       this.userFactory = BusinessTestConfigurationHolder.getUserFactory();*/
        //this.userUcc = BusinessTestConfigurationHolder.getUserUcc();
        dalServices = new DALServicesImpl();
        userFactory = new UserFactoryImpl();
        userDAO = new UserDAOImpl(dalServices, userFactory);
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