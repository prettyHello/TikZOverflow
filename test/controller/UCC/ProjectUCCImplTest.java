package controller.UCC;

import config.TestBusinessConfigurationSingleton;
import controller.Canvas.ActiveProject;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.factories.ProjectFactory;
import controller.factories.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import utilities.exceptions.FatalException;

import static org.junit.jupiter.api.Assertions.*;

//TODO fix later

class ProjectUCCImplTest {

    ProjectFactory projectFactory;
    UserFactory userFactory;
    ProjectUCC projectUcc;
    UserUCC userUcc;

    @BeforeEach
    void setUpStart() {
        TestBusinessConfigurationSingleton.getInstance();
        this.projectFactory = TestBusinessConfigurationSingleton.getProjectFactory();
        this.userFactory = TestBusinessConfigurationSingleton.getUserFactory();
        this.projectUcc = TestBusinessConfigurationSingleton.getProjectUCC();
        this.userUcc = TestBusinessConfigurationSingleton.getUserUcc();
    }

    @Test
    void createFatal(){
        assertThrows(FatalException.class, () -> {
            projectUcc.create(null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void exportFatal(){
        assertThrows(FatalException.class, () -> {
            projectUcc.export(null,null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void loadFatal(){
        assertThrows(FatalException.class, () -> {
            projectUcc.load(null,null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void loadConnectedUser(){
        ConnectedUser.deleteConnectedUser();
        UserDTO userDto = this.userFactory.createUser(10,"test", "test","e@mail.ulb","0472345261","blablabla","blablabla","blablabla");
        ConnectedUser.setConnectedUser(userDto);
        ProjectDTO projectDTO = this.projectFactory.createProject();
        projectDTO.setProjectName("testProject");
        ProjectDTO result = projectUcc.load(null,null);
        assertTrue(result.getProjectId() == projectDTO.getProjectId());
    }

    @Test
    void deleteFatal(){
        assertThrows(FatalException.class, () -> {
            projectUcc.delete(null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void saveFatal(){
        assertThrows(FatalException.class, () -> {
            ActiveProject.setActiveProject(null);
            projectUcc.save();
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void createProjectAndGetCanvas(){
        //todo
    }

    @Test
    void setActiveFatal(){
        assertThrows(FatalException.class, () -> {
            projectUcc.setActive(null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void getOwnedProjectsFatal(){
        assertThrows(FatalException.class, () -> {
            projectUcc.create(null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void getBackListOfProject(){
        assertThrows(FatalException.class, () -> {
            projectUcc.create(null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    /*
    @Test
    void setActiveFatal(){

    }

    @Test
    void setActiveFatal(){

    }

    @Test
    void setActiveFatal(){

    }

    @Test
    void setActiveFatal(){

    }

    @Test
    void setActiveFatal(){

    }

    @Test
    void setActiveFatal(){

    }
    */
}