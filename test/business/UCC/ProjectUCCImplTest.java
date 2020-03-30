package business.UCC;

import exceptions.BizzException;
import exceptions.FatalException;
import persistence.DALServices;
import persistence.DAO;
import persistence.ProjectDAO;
import utilities.TestBusinessConfigurationSingleton;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectUCCImplTest {

    DALServices dalServices;
    DAO<ProjectDAO> projectDAO;
    ProjectUCC projectUCC;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        dalServices = TestBusinessConfigurationSingleton.getDalServices();
        projectDAO = TestBusinessConfigurationSingleton.getProjectDAO();
        projectUCC = TestBusinessConfigurationSingleton.getProjectUCC();
    }

    @org.junit.jupiter.api.Test
    void createTest() {
        assertThrows(BizzException.class, () -> {
            projectUCC.createNewProject("test");
            projectUCC.createNewProject("test");
        }, "Error if a projet name is already in use");
    }

}
