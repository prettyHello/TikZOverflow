package be.ac.ulb.infof307.g09.view.dashboard;

import be.ac.ulb.infof307.g09.config.ConfigurationSingleton;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.UCC.ProjectUCC;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.view.Utility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import be.ac.ulb.infof307.g09.view.ViewName;
import be.ac.ulb.infof307.g09.view.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static be.ac.ulb.infof307.g09.view.Utility.showAlert;

public class ProjectItemView extends HBox {

    @FXML
    private Label projectName = null;
    @FXML
    private Button exportBtn = null;
    @FXML
    private Button editBtn = null;
    @FXML
    private ImageView exportIcon = null;
    @FXML
    private Button deleteBtn = null;
    @FXML
    private ImageView deleteIcon = null;
    @FXML
    private ImageView editIcon = null;
    @FXML
    private HBox projectRowHbox = null;

    private String rootFolder = File.separator + "ProjectTikZ" + File.separator;

    private ProjectUCC projectUCC = ConfigurationSingleton.getProjectUCC();
    private DashboardController dashboard;
    private ProjectDTO projectDTO;
    private ProjectFactory projectFactory = ConfigurationSingleton.getProjectFactory();

    private UserDTO userDTO;
    private ViewSwitcher viewSwitcher;

    public ProjectItemView(DashboardController dashboard, UserDTO userDTO, ProjectDTO projectDto) {
        this.projectDTO = projectDto;
        this.dashboard = dashboard;
        this.userDTO = userDTO;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("be/ac/ulb/infof307/g09/view/dashboard/viewOption.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            showAlert(Alert.AlertType.WARNING, "ViewController", "Unexpected Error", e.getMessage());
        }
    }

    public void initialize() {
        exportBtn.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save project as...");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
            fc.setInitialDirectory(new File(System.getProperty("user.home") + this.rootFolder));
            fc.setInitialFileName(this.projectDTO.getProjectName());
            File selectedFile = fc.showSaveDialog(null);
            try {
                this.projectUCC.export(selectedFile,this.projectDTO);
            }catch (FatalException e){
                showAlert(Alert.AlertType.WARNING, "Project exportation", "Unexpected Error", e.getMessage());
            }
            showAlert(Alert.AlertType.CONFIRMATION, "Success", "Success !", "File exported to : " + selectedFile.getAbsolutePath().concat(".tar.gz"));
        });

        deleteBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("confirmation ?");
            alert.setHeaderText("once deleted, the "+ this.projectDTO.getProjectName()+" project can no longer be restored");
            alert.setContentText("are you sure you want to delete");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                this.projectUCC.delete(this.projectDTO);
                this.dashboard.delete(this.projectDTO);
            }
        });

        editBtn.setOnAction(event -> {
            ProjectDTO dto = this.projectFactory.createProject();
            dto.setProjectId(this.projectDTO.getProjectId());
            String password = askProjectPassword();
            try {
                if(password != null){
                    this.projectUCC.setActive(dto, password);
                    viewSwitcher.switchView(ViewName.EDITOR);
                }

            } catch (BizzException e){
                showAlert(Alert.AlertType.WARNING, "Project opening", "Business Error", e.getMessage());
            }
            catch (FatalException e) {
                showAlert(Alert.AlertType.WARNING, "Project opening", "Unexpected Error", e.getMessage());
            }
        });
    }

    //TODO
    private String askProjectPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Project password");
        dialog.setHeaderText("Please enter password to unlock your project");
        dialog.setContentText("Password:");

        Optional<String> password = dialog.showAndWait();
        if (password.isPresent() && password.get().matches(Utility.ALLOWED_CHARACTERS_PATTERN)){
            System.out.println("Your password: " + password.get());
            return password.get();
        }
        else{
            showAlert(Alert.AlertType.WARNING, "newProject", "Please enter a valid name", "Please enter a valid name");
        }
        return null;
    }

    public void setProject(ProjectDTO projectDTO) {
        projectDTO.setProjectOwnerId(userDTO.getUserId());
        this.projectDTO = projectDTO;
        this.projectName.setText(projectDTO.getProjectName());
    }

    public void setExportIcon() {
        this.exportIcon.setImage(new Image("images/exportIcon.png"));
    }

    public void setDeleteIcon() {
        this.deleteIcon.setImage(new Image("images/deleteIcon.png"));
    }

    public void setEditIcon() {
        this.editIcon.setImage(new Image("images/edit.png"));
    }

    public HBox getProjectRowHbox() {
        return projectRowHbox;
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
