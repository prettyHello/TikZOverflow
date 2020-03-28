package view.dashboard;

import business.Canvas.ActiveProject;
import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import business.UCC.ProjectUCC;
import business.UCC.ProjectUCCImpl;
import business.UCC.ViewOptionUCCImpl;
import business.factories.ProjectFactory;
import business.factories.ProjectFactoryImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.ProjectDAO;
import persistence.ProjectDAOImpl;
import view.ViewName;
import view.ViewSwitcher;

import java.io.File;
import java.io.IOException;

public class ViewOptionController extends HBox {

    DashboardController dashboard;
    private ProjectDTO projectDTO ;


    @FXML
    private Label projectName = null;
    @FXML
    private Button exportBtn = null;
    @FXML
    private Button editBtn = null;
    @FXML
    private ImageView exportIcon = null;
    @FXML
    private Button deleteBtn = null ;
    @FXML
    private ImageView deleteIcon = null;

    @FXML
    private ImageView editIcon = null;
    @FXML
    private HBox projectRowHbox = null;

    private UserDTO user;
    private int project_id;
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    ViewOptionUCCImpl viewOptionUCC = new ViewOptionUCCImpl();
    private ViewSwitcher viewSwitcher;

    public ViewOptionController(DashboardController dashboard, UserDTO userDTO, int project_id)  {

        this.project_id = project_id;
        this.dashboard = dashboard;
        this.user = userDTO ;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/dashboard/viewOption.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {

            exportBtn.setOnAction(event -> {
                DALServices dal = new DALServicesImpl();
                ProjectFactory projectFactory = new ProjectFactoryImpl();
                ProjectDAO projectDAO = new ProjectDAOImpl(dal, projectFactory);
                ProjectDTO  chooserProject =  ((ProjectDAO) new ProjectDAOImpl(new DALServicesImpl(), new ProjectFactoryImpl())).getSelectedProject(user.getUserId(), projectName.getText());

                FileChooser fc = new FileChooser();
                fc.setTitle("Save project as...");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
                fc.setInitialDirectory(new File(System.getProperty("user.home") + rootProject));
                fc.setInitialFileName(projectName.getText() );
                File exportDirectory= fc.showSaveDialog(null);
                File dir = new File( chooserProject.getProjectPath() );
                viewOptionUCC.ExportProject(dir, exportDirectory);
            });

            deleteBtn.setOnAction(event -> {
                viewOptionUCC.deleteProject(projectDTO, dashboard);
            });

        editBtn.setOnAction(event -> {
            DALServices dal = new DALServicesImpl();
            ProjectFactory projectFactory = new ProjectFactoryImpl();
            ProjectDAO doa = new ProjectDAOImpl(dal, projectFactory);
            ProjectUCC projectUCC = new ProjectUCCImpl(dal, doa);
            ProjectDTO activeProject = projectUCC.getProjectDTO(project_id);

            ActiveProject.setActiveProject(activeProject);
            viewSwitcher.switchView(ViewName.EDITOR);
            //TODO set active canvas
            System.out.println("edit!");
        });
    }

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

    public HBox getProjectRowHbox() {
        return projectRowHbox;
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
