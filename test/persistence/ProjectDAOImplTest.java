package persistence;

import business.DTO.UserDTO;
import business.factories.ProjectFactory;
import business.factories.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.TestDAOConfigurationSingleton;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDAOImplTest {

    DALServices dalServices;
    ProjectFactory projectFactory;
    DAO<ProjectDAO> projectDAO;

    @BeforeEach
    void setUp() {
        TestDAOConfigurationSingleton holder = TestDAOConfigurationSingleton.getInstance();
        this.dalServices = TestDAOConfigurationSingleton.getDalServices();
        this.projectFactory = TestDAOConfigurationSingleton.getProjectFactory();
        this.projectDAO = TestDAOConfigurationSingleton.getProjectDAO();
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
    void testSaveNewProject(){
       // this.projectDAO.
    }
}