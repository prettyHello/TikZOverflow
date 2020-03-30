package persistence;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import business.factories.ProjectFactory;
import business.factories.UserFactory;
import exceptions.BizzException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.TestDAOConfigurationSingleton;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//TODO finish after refactoring of the implementation (the tdd & implementation was not done properly during the first iteration)
class ProjectDAOImplTest {

    DALServices dalServices;
    ProjectFactory projectFactory;
    ProjectDAO projectDAO;

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
    void testBasicInsertion(){
        ProjectDTO dto = generateBasicProjectDTO();
        projectDAO.create( dto);
        ProjectDTO result = projectDAO.get( dto);
        assertEquals(dto.getProjectName(),result.getProjectName());
        assertEquals(dto.getProjectPath(),result.getProjectPath());
        assertEquals(dto.getProjectId(),result.getProjectId());
        assertEquals(dto.getProjectOwnerId(),result.getProjectOwnerId());
        assertEquals(dto.getCreateDate(),result.getCreateDate());
        assertEquals(dto.getModificationDate(),dto.getModificationDate());
    }

    @Test
    void testInsertionOFExistingProject() {
        ProjectDTO dto = generateBasicProjectDTO();
        assertThrows(BizzException.class, () -> {
            projectDAO.create( dto);
            projectDAO.create( dto);
        }, "two identical project can be created");
    }


    ProjectDTO generateBasicProjectDTO(){
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(1);
        dto.setCreateDate("date");
        dto.setProjectOwnerId(1);
        dto.setProjectName("project");
        dto.setModificationDate("mod date");
        dto.setProjectPath("proj");
        dto.setProjectReference("ref");
        return dto;
    }

    ProjectDTO generateBasicProjectDTO2(){
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(2);
        dto.setCreateDate("date2");
        dto.setProjectOwnerId(2);
        dto.setProjectName("project2");
        dto.setModificationDate("mod date2");
        dto.setProjectPath("proj2");
        dto.setProjectReference("ref3");
        return dto;
    }

    ProjectDTO generateBasicProjectDTO3(){
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(3);
        dto.setCreateDate("date3");
        dto.setProjectOwnerId(3);
        dto.setProjectName("project3");
        dto.setModificationDate("mod date3");
        dto.setProjectPath("proj3");
        dto.setProjectReference("ref3");
        return dto;
    }
}