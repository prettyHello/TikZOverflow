package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.config.TestBusinessConfigurationSingleton;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import static org.junit.jupiter.api.Assertions.*;

class UserUCCImplTest {
    UserFactory userFactory;
    UserUCC userUcc;

    @BeforeEach
    void setUp() {
        TestBusinessConfigurationSingleton.getInstance();
        this.userFactory = TestBusinessConfigurationSingleton.getUserFactory();
        this.userUcc = TestBusinessConfigurationSingleton.getUserUcc();
    }

    @Test
    void login_nullArg() {
        assertThrows(FatalException.class, () -> {
            userUcc.login(null);
        }, "Error if a null user is passed");
    }

    @Test
    void login_wrongPassword() {
    }

    @Test
    void updateUserInfo_nullArg() {
        assertThrows(FatalException.class, () -> {
            userUcc.updateUserInfo(null);
        }, "Error if a null user is passed");
    }

    @Test
    void getUserInfo_nullArg() {
        assertThrows(FatalException.class, () -> {
            userUcc.getUserInfo(null);
        }, "Error if a null user is passed");
    }

    @Test
    void register_nullArg() {
        assertThrows(FatalException.class, () -> {
            userUcc.register(null);
        }, "Error if a null user is passed");
    }

    @Test
    void setConnectedUser_expectedBehaviour(){
        ConnectedUser.deleteConnectedUser();
        UserDTO user = userFactory.createUser();
        userUcc.setConnectedUser(user);
        assertEquals(user, ConnectedUser.getConnectedUser());
    }

    @Test
    void getConnectedUser_expectedBehaviour(){
        UserDTO user = userFactory.createUser();
        userUcc.setConnectedUser(user);
        assertEquals(user, userUcc.getConnectedUser());
    }

    @Test
    void deleteConnectedUser_expectedBehaviour(){
        ConnectedUser.deleteConnectedUser();
        assertNull(userUcc.getConnectedUser());
    }
}