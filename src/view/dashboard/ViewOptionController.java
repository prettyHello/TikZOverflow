package view.dashboard;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
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

import java.io.File;
import java.io.IOException;

public class ViewOptionController extends HBox {


    @FXML
    private Label projectName = null;
    @FXML
    private Button exportBtn = null;
    @FXML
    private Button editBtn = null;
    @FXML
    private ImageView exportIcon = null;
    @FXML
    private ImageView editIcon = null;
    @FXML
    private HBox projectRowHbox = null;

    private UserDTO user;
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    ViewOptionUCCImpl viewOptionUCC = new ViewOptionUCCImpl();

    public ViewOptionController(UserDTO userDTO) {
        this.user = userDTO;
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
            ProjectDTO chooserProject = projectDAO.getSelectedProject(user.getUser_id(), projectName.getText());

            FileChooser fc = new FileChooser();
            fc.setTitle("Save project as...");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
            fc.setInitialDirectory(new File(System.getProperty("user.home") + rootProject));
            fc.setInitialFileName(projectName.getText());
            File exportDirectory = fc.showSaveDialog(null);
            File dir = new File(chooserProject.getProjectPath());
            System.out.println("ok: " + dir);

            viewOptionUCC.Export(dir, exportDirectory);
        });

        editBtn.setOnAction(event -> {
            System.out.println("edit!");
        });
    }

    public ViewOptionController setProjectName(String projectName) {
        this.projectName.setText(projectName);
        return this;
    }

    public ViewOptionController setExportIcon(String iconUrl) {
        this.exportIcon.setImage(new Image(iconUrl));
        return this;
    }

    public ViewOptionController setEditIcon() {
        this.editIcon.setImage(new Image("images/edit.png"));
        return this;
    }

    public HBox getProjectRowHbox() {
        return projectRowHbox;
    }


}
