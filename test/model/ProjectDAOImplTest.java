package model;

import config.TestDAOConfigurationSingleton;
import controller.ProjectImpl;
import controller.factories.ProjectFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.exceptions.BizzException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        ProjectImpl dto = generateBasicProjectDTO();
        projectDAO.create( dto);
        ProjectImpl result = projectDAO.get( dto);
        assertEquals(dto.getProjectName(),result.getProjectName());
        assertEquals(dto.getProjectPath(),result.getProjectPath());
        assertEquals(dto.getProjectId(),result.getProjectId());
        assertEquals(dto.getProjectOwnerId(),result.getProjectOwnerId());
        assertEquals(dto.getCreateDate(),result.getCreateDate());
        assertEquals(dto.getModificationDate(),dto.getModificationDate());
    }

    @Test
    void testInsertionOFExistingProject() {
        ProjectImpl dto = generateBasicProjectDTO();
        assertThrows(BizzException.class, () -> {
            projectDAO.create( dto);
            projectDAO.create( dto);
        }, "two identical project can be created");
    }


    ProjectImpl generateBasicProjectDTO(){
        ProjectImpl dto = new ProjectImpl();
        dto.setProjectId(1);
        dto.setCreateDate("date");
        dto.setProjectOwnerId(1);
        dto.setProjectName("project");
        dto.setModificationDate("mod date");
        dto.setProjectPath("proj");
        dto.setProjectReference("ref");
        return dto;
    }

    ProjectImpl generateBasicProjectDTO2(){
        ProjectImpl dto = new ProjectImpl();
        dto.setProjectId(2);
        dto.setCreateDate("date2");
        dto.setProjectOwnerId(2);
        dto.setProjectName("project2");
        dto.setModificationDate("mod date2");
        dto.setProjectPath("proj2");
        dto.setProjectReference("ref3");
        return dto;
    }

    ProjectImpl generateBasicProjectDTO3(){
        ProjectImpl dto = new ProjectImpl();
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