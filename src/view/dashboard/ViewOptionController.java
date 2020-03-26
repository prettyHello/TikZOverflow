package view.dashboard;
import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import business.UCC.ViewOptionUCCImpl;
import exceptions.FatalException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import persistence.ProjectDAO;

import java.io.*;

public class ViewOptionController extends HBox {

    DashboardController dashboard;
    private ProjectDTO projectDTO ;


    @FXML
    private  Label projectName = null ;
    @FXML
    private Button exportBtn = null ;
    @FXML
    private ImageView exportIcon = null;
    @FXML
    private Button deleteBtn = null ;
    @FXML
    private ImageView deleteIcon = null;

    @FXML
    private HBox projectRowHbox  = null;

    private UserDTO user;
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    ViewOptionUCCImpl viewOptionUCC = new ViewOptionUCCImpl();

    public ViewOptionController(DashboardController dashboard, UserDTO userDTO)  {
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

    public  void  initialize() {
        try{
            exportBtn.setOnAction(event -> {
                ProjectDTO  chooserProject = ProjectDAO.getInstance().getSelectedProject(user.getUser_id(), projectName.getText());

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


        }catch(FatalException e){
            System.out.println("Fatal Exception");
        };
    }

    public ViewOptionController setProject(ProjectDTO projectDTO) {
        projectDTO.setProjectOwnerId(user.getUser_id());
        this.projectDTO = projectDTO;
        this.projectName.setText(projectDTO.getProjectName());
        return this;
    }

    public ViewOptionController setExportIcon(String iconUrl) {
        this.exportIcon.setImage(new Image(iconUrl));
        return this;
    }

    public ViewOptionController setDeleteIcon(String iconUrl) {
        this.deleteIcon.setImage(new Image(iconUrl));
        return this;
    }

    public HBox getProjectRowHbox() {
        return projectRowHbox;
    }




}
