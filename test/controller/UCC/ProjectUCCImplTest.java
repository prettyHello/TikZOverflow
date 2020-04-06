package controller.UCC;

import config.TestBusinessConfigurationSingleton;
import controller.DTO.UserDTO;
import model.DALServices;
import model.ProjectDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.exceptions.BizzException;

import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO REECRIRE apres refactor

public class ProjectUCCImplTest {

    DALServices dalServices;
    ProjectDAO projectDAO;
    ProjectUCC projectUCC;
    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        TestBusinessConfigurationSingleton.getInstance();
        this.dalServices = TestBusinessConfigurationSingleton.getDalServices();
        this.projectDAO = TestBusinessConfigurationSingleton.getProjectDAO();
        this.projectUCC = TestBusinessConfigurationSingleton.getProjectUCC();
        this.userDTO = TestBusinessConfigurationSingleton.getUserFactory().createUser(69, "t", "t","t@wsrdgw.com","0475332116","t","t", "08-03-2020 17:18:00");

    }

    @Test
    void createTest() {
        ConnectedUser.setConnectedUser(userDTO);
        assertThrows(BizzException.class, () -> {
            projectUCC.create("test");
            projectUCC.create("test");
        }, "Error if a project name is already in use");
    }

    @Test
    void createTest_NullArg() {
        ConnectedUser.setConnectedUser(userDTO);
        assertThrows(IllegalArgumentException.class, () -> {
            projectUCC.create(null);
        }, "Error if a project name argument is null");
    }

    @Test
    void createTest_EmptyArg() {
        ConnectedUser.setConnectedUser(userDTO);
        assertThrows(IllegalArgumentException.class, () -> {
            projectUCC.create("");
        }, "Error if a project name argument is empty");
    }    
}
