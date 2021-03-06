package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.config.ConfigurationHolder;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveProject;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectUCCImplTest {

    ProjectFactory projectFactory;
    UserFactory userFactory;
    ProjectUCC projectUcc;
    UserUCC userUcc;

    @BeforeEach
    void setUpStart() {
        ConfigurationHolder.loadConfiguration("TestBusiness");
        this.projectFactory = ConfigurationHolder.getProjectFactory();
        this.userFactory = ConfigurationHolder.getUserFactory();
        this.projectUcc = ConfigurationHolder.getProjectUCC();
        this.userUcc = ConfigurationHolder.getUserUcc();
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
    void setActive_nullArg(){
        assertThrows(FatalException.class, () -> {
            projectUcc.setActive(null,null);
        }, "check that the ucc don't catch the fatalException coming from the Dao");
    }
}