package view.dashboard;

import config.ConfigurationSingleton;
import controller.Canvas.ActiveCanvas;
import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.UCC.ProjectUCC;
import controller.factories.ProjectFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import utilities.exceptions.FatalException;
import view.ViewName;
import view.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static utilities.Utility.showAlert;

//import model.SaveObject;

//TODO CLASS : rename to something actually correct and not meaningless

public class ViewOptionController extends HBox {
    private String rootFolder = File.separator + "ProjectTikZ" + File.separator;

    private ProjectUCC projectUCC = ConfigurationSingleton.getProjectUCC();
    private DashboardController dashboard;
    private ProjectDTO projectDTO;
    private ProjectFactory projectFactory = ConfigurationSingleton.getProjectFactory();

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

    private UserDTO userDTO;
    private ProjectDTO projectDto;
    private ViewSwitcher viewSwitcher;

    public ViewOptionController(DashboardController dashboard, UserDTO userDTO, ProjectDTO projectDto) {
        this.projectDTO = projectDto;
        this.dashboard = dashboard;
        this.userDTO = userDTO;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/dashboard/viewOption.fxml"));
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
            showAlert(Alert.AlertType.CONFIRMATION,"Success","Success !!!!!","File exported to : " + selectedFile.getAbsolutePath().concat(".tar.gz"));
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
            Canvas canvas = null;
            try {
                this.projectUCC.setActive(dto);
                canvas = this.projectUCC.loadSavedCanvas();
            } catch (FatalException e) {
                showAlert(Alert.AlertType.WARNING, "Project openning", "Unexpected Error", e.getMessage());
            }
            if (canvas != null) {
                ActiveCanvas.setActiveCanvas(canvas);
                viewSwitcher.switchView(ViewName.EDITOR);
            }
        });
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
