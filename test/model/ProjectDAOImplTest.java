package model;

import config.TestDAOConfigurationSingleton;
import controller.Canvas.Canvas;
import controller.Canvas.CanvasImpl;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.factories.ProjectFactory;
import controller.factories.UserFactory;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;

class ProjectDAOImplTest {

    DALServices dalServices;
    ProjectFactory projectFactory;
    ProjectDAO projectDAO;
    UserFactory userFactory;
    DAO<UserDTO> userDAO;
    private String rootFolder = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator + "tests" + File.separator ;

    @BeforeEach
    void setUp() {
        TestDAOConfigurationSingleton holder = TestDAOConfigurationSingleton.getInstance();
        this.dalServices = TestDAOConfigurationSingleton.getDalServices();
        this.projectFactory = TestDAOConfigurationSingleton.getProjectFactory();
        this.projectDAO = TestDAOConfigurationSingleton.getProjectDAO();
        this.userFactory = TestDAOConfigurationSingleton.getUserFactory();
        this.userDAO = TestDAOConfigurationSingleton.getUserDAO();

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
        ProjectDTO projectDTO = generateBasicProjectDTO();
        UserDTO userDTO = generateBasicUserDTO();
        Canvas c = generateDummyCanvas();
        assertThrows(FatalException.class, () -> {
            projectDAO.save(c, projectDTO);
        }, "saving an innexisting project works");
    }

    @Test
    void saveExistingProject(){
        ProjectDTO projectDTO = generateBasicProjectDTO();
        UserDTO userDTO = generateBasicUserDTO();
        Canvas c = generateDummyCanvas();
        projectDAO.create(projectDTO);
        projectDAO.save(c, projectDTO);
        Path destination = Paths.get(projectDTO.getProjectPath()+ File.separator + projectDTO.getProjectName() + ".bin");
        assertTrue(Files.exists(destination), "project folder wasn't deleted after delete was called");
    }

    @Test
    void exportExistingProject(){
        String exportPath = rootFolder+"export";
        File fileToBeCreated = new File(exportPath);
        ProjectDTO projectDTO = generateBasicProjectDTO();
        projectDAO.create(projectDTO);
        projectDAO.export(fileToBeCreated, projectDTO);
        Path destination = Paths.get(exportPath+".tar.gz");
        assertTrue(Files.exists(destination), "archive was not created");

    }

    @Test
    void exportNonExistingProject(){
        String exportPath = rootFolder+"export";
        File fileToBeCreated = new File(exportPath);
        ProjectDTO projectDTO = generateBasicProjectDTO();
        assertThrows(FatalException.class, () -> {
            projectDAO.export(fileToBeCreated, projectDTO);
        }, "exporting an nonexisting project should have raised a fatalexception");

        Path destination = Paths.get(exportPath+".tar.gz");
        assertFalse(Files.exists(destination), "archive was created while the project doesn't exist");
    }

    @Test
    void importEmptyArchive(){
        UserDTO userDTO = generateBasicUserDTO();
        userDTO.setUserId(666666); //TODO move the rootFolderInPorjectDAOImpl vers la conf
        userDAO.create(userDTO);
        ProjectDTO projectDTO = projectFactory.createProject();
        projectDTO.setProjectName("test");
        File archive = new File(generateExportedEmptyPorject(generateBasicProjectDTO2()));//TODO ??
        assertThrows(BizzException.class, () -> {
            projectDAO.load(archive,projectDTO,userDTO);
        }, "we expect that an exception is launch if the archive is empty");
        cleanImport(userDTO.getUserId());
    }

    @Test
    void workingImport(){
        UserDTO userDTO = generateBasicUserDTO();
        userDTO.setUserId(666666); //TODO move the rootFolderInPorjectDAOImpl vers la conf
        userDAO.create(userDTO);
        ProjectDTO projectDTO = projectFactory.createProject();
        projectDTO.setProjectName("test");
        File archive = new File(generateExportedFilledProject(userDTO,generateBasicProjectDTO2()));
        projectDAO.load(archive,projectDTO,userDTO);
        Path ExpectedResult = Paths.get(System.getProperty("user.home")  + File.separator +"userid_" +userDTO.getUserId() + File.separator
                + projectDTO.getProjectName() + File.separator + projectDTO.getProjectName()+".bin");
        assertTrue(Files.exists(ExpectedResult));
        cleanImport(userDTO.getUserId());
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

    /**
     * create a empty tar.gz
     * @return the path of the tar.gz
     */
    String generateExportedEmptyPorject(ProjectDTO projectDTO){
        String exportPath = rootFolder+"export";
        File fileToBeCreated = new File(exportPath);
        projectDAO.create(projectDTO);
        projectDAO.export(fileToBeCreated, projectDTO);
        return exportPath+".tar.gz";
    }

    /**
     * create a tar.gz with a canvas saved inside (.bin)
     * @return the path of the tar.gz
     */
    String generateExportedFilledProject(UserDTO userDTO, ProjectDTO projectDTO){
        String exportPath = rootFolder+"export";
        File fileToBeCreated = new File(exportPath);
        Canvas c = generateDummyCanvas();
        projectDAO.create(projectDTO);
        projectDAO.save(c, projectDTO);
        projectDAO.export(fileToBeCreated, projectDTO);
        return exportPath+".tar.gz";
    }



    /**
     * the import is different than the rest as he is not given the path
     *  clean the dummy folder
     * @param userId name of the dummy folder
     */
    private void cleanImport(int userId){
        Utility.deleteFileSilent(new File(System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator +"userid_"+ userId));
    }
}