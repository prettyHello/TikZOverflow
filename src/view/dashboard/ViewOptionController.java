package view.dashboard;

import config.ConfigurationSingleton;
import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.UCC.ProjectUCC;
import controller.UCC.UserUCC;
import controller.UCC.ViewOptionUCCImpl;
import controller.factories.ProjectFactory;
import controller.factories.ProjectFactoryImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
//TODO WTF
//import model.*;
import utilities.Utility;
import view.ViewName;
import view.ViewSwitcher;

import java.io.File;
import java.io.IOException;


//TODO refactor to be MVC compliant
//TODO CLASS : rename to something actually correct and not meaningless,
//TODO REFACTOR TO USE SINGLETON CONFIGURATION (check dashboardController)

public class ViewOptionController extends HBox {

    private ProjectUCC projectUCC;
    private UserUCC userUcc;
    private DashboardController dashboard;
    private ProjectDTO projectDTO;


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

    private UserDTO user;
    private int project_id;
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    private ViewOptionUCCImpl viewOptionUCC;
    private ViewSwitcher viewSwitcher;

    //TODO project id  -> dto
    public ViewOptionController(DashboardController dashboard, UserDTO userDTO, int project_id) {
        this.projectUCC = ConfigurationSingleton.getProjectUCC();
        this.userUcc = ConfigurationSingleton.getUserUcc();
        this.viewOptionUCC = new ViewOptionUCCImpl();
        this.project_id = project_id;
        this.dashboard = dashboard;
        this.user = userDTO;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/dashboard/viewOption.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            //TODO pas ça please
            e.printStackTrace();
        }
    }

    public void initialize() {
        //TODO SORTIR LES HANDLER DE INIT
        exportBtn.setOnAction(event -> {
            DALServices dal = new DALServicesImpl();
            ProjectFactory projectFactory = new ProjectFactoryImpl();
            ProjectDAO projectDAO = new ProjectDAOImpl(dal, projectFactory);
            ProjectDTO chooserProject = ((ProjectDAO) new ProjectDAOImpl(new DALServicesImpl(), new ProjectFactoryImpl())).getSelectedProject(user.getUserId(), projectName.getText());

            FileChooser fc = new FileChooser();
            fc.setTitle("Save project as...");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
            fc.setInitialDirectory(new File(System.getProperty("user.home") + rootProject));
            fc.setInitialFileName(projectName.getText());
            File exportDirectory = fc.showSaveDialog(null);
            File dir = new File(chooserProject.getProjectPath());
            viewOptionUCC.ExportProject(dir, exportDirectory);
        });

        deleteBtn.setOnAction(event -> {
            viewOptionUCC.deleteProject(projectDTO, dashboard);
        });

        editBtn.setOnAction(event -> {
            ProjectDTO project= new ProjectDTO();
            project.setProjectId(project_id);
            UserDTO user = userUcc.getConnectedUser();
            ProjectDTO activeProject = projectUCC.get(project);

            ActiveProject.setActiveProject(activeProject);
            SaveObject loader = new SaveObject();
            Canvas loaded = null;
            try {
                loaded = loader.open(activeProject.getProjectName(), user);
            } catch (IOException | ClassNotFoundException e) {
                Utility.showAlert(Alert.AlertType.ERROR, "Fatal error", "Internal error", "Error during loading of the project");
            }
            if (loaded != null) {
                ActiveCanvas.setActiveCanvas(loaded);
                viewSwitcher.switchView(ViewName.EDITOR);
            }
        });
    }

    //Todo SUSPECT
    public ViewOptionController setProject(ProjectDTO projectDTO) {
        projectDTO.setProjectOwnerId(user.getUserId());
        this.projectDTO = projectDTO;
        this.projectName.setText(projectDTO.getProjectName());
        return this;
    }

    public ViewOptionController setExportIcon() {
        this.exportIcon.setImage(new Image("images/exportIcon.png"));
        return this;
    }

    public ViewOptionController setDeleteIcon() {
        this.deleteIcon.setImage(new Image("images/deleteIcon.png"));
        return this;
    }

    public void setEditIcon() {
        this.editIcon.setImage(new Image("images/edit.png"));
    }

    //Todo SUSPECT ?
    public HBox getProjectRowHbox() {
        return projectRowHbox;
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
