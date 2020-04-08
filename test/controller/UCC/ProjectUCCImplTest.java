package controller.UCC;

import config.TestBusinessConfigurationSingleton;
import controller.Canvas.ActiveProject;
import controller.factories.ProjectFactory;
import controller.factories.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import utilities.exceptions.FatalException;

import static org.junit.jupiter.api.Assertions.*;

class ProjectUCCImplTest {

    ProjectFactory projectFactory;
    ProjectUCC projectUcc;

    @BeforeAll
    void setUpStart() {
        TestBusinessConfigurationSingleton.getInstance();
        this.projectFactory = TestBusinessConfigurationSingleton.getProjectFactory();
        this.projectUcc = TestBusinessConfigurationSingleton.getProjectUCC();
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
    void loadSavedCanvasFatal(){
        assertThrows(FatalException.class, () -> {
            ActiveProject.setActiveProject(null);
            projectUcc.loadSavedCanvas();
        }, "check that the ucc don't catch the fatalException coming from the Dao");
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