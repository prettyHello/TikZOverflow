package business.UCC;

import business.DTO.UserDTO;
import business.factories.UserFactory;
import exceptions.FatalException;
import persistence.DALServices;
import persistence.DAO;
import utilities.TestBusinessConfigurationSingleton;

import static org.junit.jupiter.api.Assertions.*;


class UserUCCImplTest {
    DALServices dalServices;
    UserFactory userFactory;
    DAO<UserDTO> userDAO;
    UserUCC userUcc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        TestBusinessConfigurationSingleton.getInstance();
        this.dalServices = TestBusinessConfigurationSingleton.getDalServices();
        this.userDAO = TestBusinessConfigurationSingleton.getUserDAO();
        this.userFactory = TestBusinessConfigurationSingleton.getUserFactory();
        this.userUcc = TestBusinessConfigurationSingleton.getUserUcc();
    }

    @org.junit.jupiter.api.Test
    void login_nullArg() {
        assertThrows(FatalException.class, () -> {
            userUcc.login(null);
        }, "Error if a null user is passed");
    }

    @org.junit.jupiter.api.Test
    void login_wrongPassword() {
    }

    @org.junit.jupiter.api.Test
    void updateUserInfo_nullArg() {
        assertThrows(FatalException.class, () -> {
            userUcc.updateUserInfo(null);
        }, "Error if a null user is passed");
    }

    @org.junit.jupiter.api.Test
    void getUserInfo_nullArg() {
        assertThrows(FatalException.class, () -> {
            userUcc.getUserInfo(null);
        }, "Error if a null user is passed");
    }

    @org.junit.jupiter.api.Test
    void register_nullArg() {
        assertThrows(FatalException.class, () -> {
            userUcc.register(null);
        }, "Error if a null user is passed");
    }

    @org.junit.jupiter.api.Test
    void setConnectedUser(){
        ConnectedUser.deleteConnectedUser();
        UserDTO user = userFactory.createUser();
        userUcc.setConnectedUser(user);
        assertEquals(user, ConnectedUser.getConnectedUser());
    }

    @org.junit.jupiter.api.Test
    void getConnectedUser(){
        UserDTO user = userFactory.createUser();
        userUcc.setConnectedUser(user);
        assertEquals(user, userUcc.getConnectedUser());
    }

    @org.junit.jupiter.api.Test
    void deleteConnectedUser(){
        ConnectedUser.deleteConnectedUser();
        assertNull(userUcc.getConnectedUser());
    }
}