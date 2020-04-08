package model;

import config.TestDAOConfigurationSingleton;
import controller.Canvas.Canvas;
import controller.Canvas.CanvasImpl;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.factories.ProjectFactory;
import controller.factories.UserFactory;
import controller.shape.Shape;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDAOImplTest {

    DALServices dalServices;
    ProjectFactory projectFactory;
    ProjectDAO projectDAO;
    UserFactory userFactory;
    private String rootFolder = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator + "tests" + File.separator ;

    @BeforeEach
    void setUp() {
        TestDAOConfigurationSingleton holder = TestDAOConfigurationSingleton.getInstance();
        this.dalServices = TestDAOConfigurationSingleton.getDalServices();
        this.projectFactory = TestDAOConfigurationSingleton.getProjectFactory();
        this.projectDAO = TestDAOConfigurationSingleton.getProjectDAO();
        this.userFactory = TestDAOConfigurationSingleton.getUserFactory();

        try {
            dalServices.createTables("dao_test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        dalServices.deleteDB("dao_test");
        Utility.deleteFileSilent(new File(rootFolder));
    }


    @Test
    void testBasicInsertion(){
        ProjectDTO dto = generateBasicProjectDTO();
        projectDAO.create(dto);
        ProjectDTO result = projectDAO.get(dto);
        assertEquals(dto.getProjectName(),result.getProjectName());
        assertEquals(dto.getProjectPath(),result.getProjectPath());
        assertEquals(dto.getProjectId(),result.getProjectId());
        assertEquals(dto.getProjectOwnerId(),result.getProjectOwnerId());
        assertEquals(dto.getCreateDate(),result.getCreateDate());
        assertEquals(dto.getModificationDate(),dto.getModificationDate());

        Path destination = Paths.get(dto.getProjectPath());
        assertTrue(Files.exists(destination),"The project folder wasn't created");
    }

    @Test
    void testInsertionSameName() {
        ProjectDTO dto = generateBasicProjectDTO();
        ProjectDTO dto2 = generateBasicProjectDTO2();
        dto2.setProjectName(dto.getProjectName());
        assertThrows(FatalException.class, () -> {
            projectDAO.create( dto);
            projectDAO.create( dto2);
        }, "two project with same name can be created");

        Path destination = Paths.get(dto2.getProjectPath());
        assertFalse(Files.exists(destination), "project folder was created despite 2 project with same name");
    }

    @Test
    void testInsertionSamePath() {
        ProjectDTO dto = generateBasicProjectDTO();
        ProjectDTO dto2 = generateBasicProjectDTO2();
        dto2.setProjectPath(dto.getProjectPath());
        projectDAO.create( dto);
        assertThrows(FatalException.class, () -> {
            projectDAO.create( dto2);
        }, "two project with same path can be created");
    }

    @Test
    void testBasicDelete() {
        ProjectDTO dto = generateBasicProjectDTO();
        projectDAO.create( dto);
        projectDAO.delete(dto);
        assertThrows(BizzException.class, () -> {
            projectDAO.get(dto);
        }, "the project is accessible after delete");

        Path destination = Paths.get(dto.getProjectPath());
        assertFalse(Files.exists(destination), "project folder wasn't deleted after delete was called");
    }

    @Test
    void saveNonexistingProject(){

        UserDTO userDTO = generateBasicUserDTO();
        Canvas c = generateDummyCanvas();
        /*assertThrows(FatalException.class, () -> {
            projectDAO.save(c,userDTO);
        }, "saving an innexisting project works");*/
    }

    Canvas generateDummyCanvas(){
        return new CanvasImpl(1,1);
    }

    ProjectDTO generateBasicProjectDTO(){
        ProjectDTO dto = projectFactory.createProject();
        dto.setProjectId(1);
        dto.setCreateDate("date");
        dto.setProjectOwnerId(1);
        dto.setProjectName("project");
        dto.setModificationDate("mod date");
        dto.setProjectPath(rootFolder+"project");
        return dto;
    }

    ProjectDTO generateBasicProjectDTO2(){
        ProjectDTO dto = projectFactory.createProject();
        dto.setProjectId(2);
        dto.setCreateDate("date2");
        dto.setProjectOwnerId(2);
        dto.setProjectName("project2");
        dto.setModificationDate("mod date2");
        dto.setProjectPath(rootFolder+"project2");
        return dto;
    }

    ProjectDTO generateBasicProjectDTO3(){
        ProjectDTO dto = projectFactory.createProject();
        dto.setProjectId(3);
        dto.setCreateDate("date3");
        dto.setProjectOwnerId(3);
        dto.setProjectName("project3");
        dto.setModificationDate("mod date3");
        dto.setProjectPath(rootFolder+"project3");
        return dto;
    }

    private UserDTO generateBasicUserDTO() {
        UserDTO user = userFactory.createUser();
        user.setFirstName("ben");
        user.setPassword("pass");
        user.setSalt("salt");
        user.setEmail("mail@mail.be");
        user.setLastName("ber");
        user.setPhone("123");
        user.setRegisterDate(LocalDateTime.now().toString());
        return user;
    }
}