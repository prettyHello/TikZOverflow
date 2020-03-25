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
import persistence.ImportExportDAO;

import java.io.*;

public class ViewOptionController extends HBox {


    @FXML
    private  Label projectName = null ;
    @FXML
    private Button exportBtn = null ;
    @FXML
    private ImageView exportIcon = null;
    @FXML
    private HBox projectRowHbox  = null;

    private UserDTO user;
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    ViewOptionUCCImpl viewOptionUCC = new ViewOptionUCCImpl();

    public ViewOptionController(UserDTO userDTO)  {
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
                ProjectDTO  chooserProject = ImportExportDAO.getInstance().getSelectedProject(user.getUser_id(), projectName.getText());

                FileChooser fc = new FileChooser();
                fc.setTitle("Save project as...");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
                fc.setInitialDirectory(new File(System.getProperty("user.home") + rootProject));
                fc.setInitialFileName(projectName.getText() );
                File exportDirectory= fc.showSaveDialog(null);
                File dir = new File( chooserProject.getProjectPath() );
                viewOptionUCC.ExportProject(dir, exportDirectory);
            });
        }catch(FatalException e){
            System.out.println("Fatal Exception");
        };
    }

    public ViewOptionController setProjectName(String projectName) {
        this.projectName.setText(projectName);
        return this;
    }

    public ViewOptionController setExportIcon(String iconUrl) {
        this.exportIcon.setImage(new Image(iconUrl));
        return this;
    }

    public HBox getProjectRowHbox() {
        return projectRowHbox;
    }




}
