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

    private String popupMessage = "Please enter the name of your Project" ;
    private String rootProject = "/ProjectTikZ/";
    private String ContentTextImport = "impossible to import, this project already exists in: ";


    @Override
    public void renameFolderProject(File projectName, File NewProjectName) {
        projectName.renameTo(NewProjectName);

    }

    @Override
    public String setProjectName(String popupMessage) throws BizzException {
        Optional<String> projectName;

        TextInputDialog enterProjectName = new TextInputDialog();
        enterProjectName.setTitle("Project name");
        enterProjectName.setHeaderText(popupMessage);
        enterProjectName.setContentText("Name :");
        projectName = enterProjectName.showAndWait();
        while(projectName.isPresent() && !projectName.get().matches(Utility.ALLOWED_CHARACTERS_PATTERN)){
            new Alert(Alert.AlertType.ERROR, ContentTextImport  ).showAndWait();
            enterProjectName.setTitle("Project name");
            enterProjectName.setHeaderText(popupMessage);
            enterProjectName.setContentText("Name :");
            projectName = enterProjectName.showAndWait();
            System.out.println("Specials Characters Not ALLOWED!!!");
            System.out.println(!projectName.get().matches(Utility.ALLOWED_CHARACTERS_PATTERN));

        }
        return projectName.get();
    }

    @Override
    public ProjectDTO getProjectDTO(String projectName, Path folderDestination, int userId) {
        return  new ProjectDTO().
                setProjectOwnerId(userId)
                .setProjectName(projectName)
                .setProjectPath(folderDestination.toString()+"/"+projectName)
                .setCreateDate(Utility.getTimeStamp())
                .setModificationDate(Utility.getTimeStamp());
    }

}
