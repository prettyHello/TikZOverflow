package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.config.ConfigurationHolder;
import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.Canvas.CanvasImpl;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private final String rootFolder = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator + "tests" + File.separator;

    @BeforeEach
    void setUp() {
        ConfigurationHolder.loadConfiguration("TestDAO");
        this.dalServices = ConfigurationHolder.getDalServices();
        this.projectFactory = ConfigurationHolder.getProjectFactory();
        this.projectDAO = ConfigurationHolder.getProjectDAO();
        this.userFactory = ConfigurationHolder.getUserFactory();
        this.userDAO = ConfigurationHolder.getUserDAO();

        try {
            dalServices.createTables("dao_test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        dalServices.deleteDB("dao_test");
        ModelUtility.deleteFileSilent(new File(rootFolder)); // zip folder
    }


    @Test
    void create_expectedBehaviour(){
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
    void create_pathUniqueness() {
        ProjectDTO dto = generateBasicProjectDTO();
        ProjectDTO dto2 = generateBasicProjectDTO2();
        dto2.setProjectPath(dto.getProjectPath());
        projectDAO.create( dto);
        assertThrows(FatalException.class, () -> {
            projectDAO.create( dto2);
        }, "two project with same path can be created");
    }

    @Test
    void delete_expectedBehaviour() {
        ProjectDTO dto = generateBasicProjectDTO();
        projectDAO.create( dto);
        projectDAO.delete(dto);
        assertThrows(FatalException.class, () -> {
            projectDAO.get(dto);
        }, "the project is accessible via getter after delete");

        Path destination = Paths.get(dto.getProjectPath());
        assertFalse(Files.exists(destination), "project folder wasn't deleted after delete was called");
    }

    @Test
    void save_nonExistingProject(){
        ProjectDTO projectDTO = generateBasicProjectDTO();
        Canvas c = generateDummyCanvas();
        assertThrows(FatalException.class, () -> {
            projectDAO.save(c, projectDTO);
        }, "saving an innexisting project works");
    }

    @Test
    void save_existingProject(){
        ProjectDTO projectDTO = generateBasicProjectDTO();
        Canvas c = generateDummyCanvas();
        projectDAO.create(projectDTO);
        projectDAO.save(c, projectDTO);
        Path destination = Paths.get(projectDTO.getProjectPath()+ File.separator + projectDTO.getProjectName() + ".bin.enc");
        assertTrue(Files.exists(destination), "project folder wasn't created after save was called");
    }

    @Test
    void checkEncryptedFile(){
        ProjectDTO projectDTO = generateBasicProjectDTO();
        Canvas c = generateDummyCanvas();
        projectDAO.create(projectDTO);
        projectDAO.save(c, projectDTO);
        Path destination = Paths.get(projectDTO.getProjectPath()+ File.separator + projectDTO.getProjectName() + ".bin.enc");
        assertTrue(Files.exists(destination), "project file wasn't encrypted");
    }

    @Test
    void checkDecryptedFile(){
        ProjectDTO projectDTO = generateBasicProjectDTO();
        projectDTO.setProjectPassword("test");
        Canvas c = generateDummyCanvas();
        projectDAO.create(projectDTO);
        projectDAO.save(c, projectDTO);
        Path destination = Paths.get(projectDTO.getProjectPath());
        Crypto.decryptDirectory("test", destination.toString());
        Path destination2 = Paths.get(projectDTO.getProjectPath()+ File.separator + projectDTO.getProjectName() + ".bin");
        assertTrue(Files.exists(destination2), "project file wasn't decrypted");
    }

    @Test
    void export_expectedBehaviour(){
        String exportPath = rootFolder+"encryption";
        File fileToBeCreated = new File(exportPath);
        ProjectDTO projectDTO = generateBasicProjectDTO();
        projectDAO.create(projectDTO);
        projectDAO.export(fileToBeCreated, projectDTO);
        Path destination = Paths.get(exportPath+".tar.gz");
        assertTrue(Files.exists(destination), "archive was not created");
    }

    @Test
    void export_nonExistingProject(){
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
    void import_emptyArchive(){
        UserDTO userDTO = generateBasicUserDTO();
        userDTO.setUserId(Integer.MAX_VALUE);
        userDAO.create(userDTO);
        ProjectDTO projectDTO = projectFactory.createProject();
        projectDTO.setProjectName("test");
        File archive = new File(generateExportedEmptyPorject(generateBasicProjectDTO2()));
        assertThrows(BizzException.class, () -> {
            projectDAO.load(archive,projectDTO,userDTO);
        }, "we expect that an exception is launch if the archive is empty");
        cleanImport(userDTO.getUserId());
    }

    @Test
    void import_expectedBehaviour(){
        UserDTO userDTO = generateBasicUserDTO();
        userDTO.setUserId(Integer.MAX_VALUE);
        userDAO.create(userDTO);
        ProjectDTO projectDTO = generateBasicProjectDTO();
        projectDTO.setProjectName("test");
        File archive = new File(generateExportedFilledProject(generateBasicProjectDTO2()));
        projectDAO.load(archive,projectDTO,userDTO);
        Path expectedResult = Paths.get(System.getProperty("user.home") + File.separator + "ProjectTikZ"  + File.separator +"userid_" +userDTO.getUserId() + File.separator
                + projectDTO.getProjectName() + File.separator + projectDTO.getProjectName()+".bin.enc");
        assertTrue(Files.exists(expectedResult),"The import doesn't seems to generate the .bin.enc");
        assertNotNull(this.projectDAO.get(projectDTO), "The import hasn't created the project in the database");
        cleanImport(userDTO.getUserId());
    }

    @Test
    void loadSavedCanvas_expectedBehaviour(){
        ProjectDTO projectDTO = generateFilledProject(generateBasicProjectDTO());
        Canvas c = this.projectDAO.loadSavedCanvas(projectDTO,"");
        assertNotNull(c, "The canvas return for an existing .bin is empty");
    }

    @Test
    void loadSavedCanvas_fileWasTemperedWith(){
        ProjectDTO projectDTO = generateFilledProject(generateBasicProjectDTO());
        File f = new File(projectDTO.getProjectPath() + File.separator + projectDTO.getProjectName() + ".bin.enc");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            out.write(123);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThrows(BizzException.class, () -> {
            Canvas c = this.projectDAO.loadSavedCanvas(projectDTO,"");
        },"The file has been tempered with, thus an exception should be raised");
    }

    /**
     * This test ensure that if the file .bin was never saved or was deleted, the method create a new canvas
     */
    @Test
    void loadSavedCanvas_createNewCanvas(){
        ProjectDTO projectDTO = generateBasicProjectDTO();
        ProjectDTO result = projectDAO.create(projectDTO);
        Canvas c = this.projectDAO.loadSavedCanvas(projectDTO,"");
        assertNotNull(c, "if the .bin was deleted or not saved, the method is supposed to create a new Canvas");
        assertEquals(result.getProjectId(),1, "The projectDTO returned was filled with the new id");
    }

    private Canvas generateDummyCanvas(){
        return new CanvasImpl();
    }

    private ProjectDTO generateBasicProjectDTO(){
        ProjectDTO dto = projectFactory.createProject();
        dto.setProjectId(1);
        dto.setCreateDate("date");
        dto.setProjectOwnerId(1);
        dto.setProjectName("project");
        dto.setModificationDate("mod date");
        dto.setProjectPath(rootFolder+"project");
        return dto;
    }

    private ProjectDTO generateBasicProjectDTO2(){
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
    private String generateExportedEmptyPorject(ProjectDTO projectDTO){
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
    private String generateExportedFilledProject(ProjectDTO projectDTO){
        String exportPath = rootFolder+"export";
        File fileToBeCreated = new File(exportPath);
        Canvas c = generateDummyCanvas();
        projectDAO.create(projectDTO);
        projectDAO.save(c, projectDTO);
        projectDAO.export(fileToBeCreated, projectDTO);
        return exportPath+".tar.gz";
    }

    private ProjectDTO generateFilledProject(ProjectDTO projectDTO){
        Canvas c = generateDummyCanvas();
        projectDAO.create(projectDTO);
        projectDAO.save(c, projectDTO);
        return projectDTO;
    }



    /**
     * the import is different than the rest as he is not given the path
     *  clean the dummy folder
     * @param userId name of the dummy folder
     */
    private void cleanImport(int userId){
        ModelUtility.deleteFileSilent(new File(System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator +"userid_"+ userId));
    }
}