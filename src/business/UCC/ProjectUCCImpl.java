package business.UCC;
import business.DTO.ProjectDTO;
import exceptions.BizzException;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import utilities.Utility;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;


public class ProjectUCCImpl implements ProjectUCC {

    private String ContentTextImport = "impossible to import, name contains unauthorized characters... ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void renameFolderProject(File projectName, File NewProjectName) {
        System.out.println("Src =" + projectName);
        System.out.println("Dst = "+ NewProjectName);
        projectName.renameTo(NewProjectName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String setProjectName(String popupMessage) throws BizzException {
        Optional<String> projectName;

        TextInputDialog enterProjectName = new TextInputDialog();
        enterProjectName.setTitle("Project name");
        enterProjectName.setHeaderText(popupMessage);
        enterProjectName.setContentText("Name :");
        projectName = enterProjectName.showAndWait();
        if (projectName.isPresent() ) {
            if (projectName.get().matches(Utility.ALLOWED_CHARACTERS_PATTERN ) ) {
                return projectName.get();
            }else {
                new Alert(Alert.AlertType.ERROR, ContentTextImport).showAndWait();
            }
        }
        return null ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO getProjectDTO(String projectName, Path folderDestination, int userId) {
        return  new ProjectDTO().
                setProjectOwnerId(userId)
                .setProjectName(projectName)
                .setProjectPath(folderDestination.toString()+ File.separator +projectName)
                .setCreateDate(Utility.getTimeStamp())
                .setModificationDate(Utility.getTimeStamp());
    }

}
