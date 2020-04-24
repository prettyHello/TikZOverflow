package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.config.TestBusinessConfigurationSingleton;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveProject;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void create_nullArg(){
        assertThrows(FatalException.class, () -> {
            projectUcc.create(null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void export_nullArg(){
        assertThrows(FatalException.class, () -> {
            projectUcc.export(null,null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void load_nullArg(){
        assertThrows(FatalException.class, () -> {
            projectUcc.load(null,null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void delete_nullArg(){
        assertThrows(FatalException.class, () -> {
            projectUcc.delete(null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }

    @Test
    void save_nullArg(){
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
    void setActive_nullArg(){
        assertThrows(FatalException.class, () -> {
            projectUcc.setActive(null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }
}