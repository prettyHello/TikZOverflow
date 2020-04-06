package controller.UCC;

import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.DTO.ProjectDTO;
import controller.ProjectImpl;
import controller.DTO.UserDTO;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import model.DALServices;
import model.DAO;
import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static utilities.Utility.checkObjects;
import static utilities.Utility.checkString;

/**
 * {@inheritDoc}
 */
public class ProjectUCCImpl implements ProjectUCC {


    private String rootProject = File.separator + "ProjectTikZ" + File.separator;

    private final DALServices dal;
    private final DAO<ProjectDTO> projectDAO;

    public ProjectUCCImpl(DALServices dalServices, DAO<ProjectDTO> projectDAO) {
        this.dal = dalServices;
        this.projectDAO = projectDAO;
    }

    //TODO WHY PUBLIC ?
    /**
     * {@inheritDoc}
     */
    @Override
    public void renameFolderProject(File projectName, File NewProjectName) {
        projectName.renameTo(NewProjectName);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void create(ProjectDTO dto) throws BizzException, FatalException {
        checkObjects(dto);
        String projectName = dto.getProjectName();
        checkString(projectName, "project Name");

        try {
            dal.startTransaction();
            UserDTO owner = ConnectedUser.getConnectedUser();
            String projectPath = System.getProperty("user.home") + rootProject +"userid_" +owner.getUserId() + File.separator + projectName;
            dto.setProjectOwnerId(owner.getUserId());
            dto.setCreateDate(Utility.getTimeStamp());
            dto.setModificationDate(Utility.getTimeStamp());
            dto.setProjectPath(projectPath);
            this.projectDAO.create(dto);
            //TODO move in dao
            Files.createDirectories(Paths.get(projectPath));
            ActiveProject.setActiveProject(dto);
            ActiveCanvas.setNewCanvas(-1, -1);

            dal.commit();
        } catch (IOException e) { //TODO move once file in dao
            throw new FatalException("Couldn't create project. Error failed to create a new directory.");
        } finally {
            dal.rollback();
        }

    }


    /**
     * {@inheritDoc}
     */
    //TODO file in dao
    public void ExportProject(ProjectDTO dto) {
        dto = projectDAO.get(dto);
        FileChooser fc = new FileChooser();
        fc.setTitle("Save project as...");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
        fc.setInitialDirectory(new File(System.getProperty("user.home") + this.rootProject));
        fc.setInitialFileName(dto.getProjectName());
        File selectedFile = fc.showSaveDialog(null);
        File dir = new File(dto.getProjectPath());

        try {
            if ( selectedFile != null ) {
                if (dir.exists()) {
                    if ( Utility.createTarGz(dir.toString(), selectedFile.getAbsolutePath().concat(".tar.gz") ) ) {
                        new Alert(Alert.AlertType.CONFIRMATION, "File exported to : " + selectedFile.getAbsolutePath().concat(".tar.gz")).showAndWait();
                    }
                    else {
                        Utility.deleteFile(new File(selectedFile.getAbsolutePath().concat(".tar.gz") ) );
                        new Alert(Alert.AlertType.ERROR, "Too long path to a certain file ( > 100 bytes)").showAndWait();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error Export the project does not exist on the path: " +dir ).showAndWait();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        catch (BizzException e){
            e.getMessage();
        }
    }

}

