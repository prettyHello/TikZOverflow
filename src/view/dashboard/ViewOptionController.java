package view.dashboard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class ViewOptionController extends HBox {
    @FXML
    private  Label projectName = null ;
    @FXML
    private Button exportBtn = null ;
    @FXML
    private ImageView exportIcon = null;
    @FXML
    private HBox projectRowHbox  = null;

    public ViewOptionController()  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/dashboard/viewOption.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void  initialize(){
        exportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                File selectedFile= fc.showSaveDialog(null);
            }
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

    public HBox getProjectRowHbox() {
        return projectRowHbox;
    }

    public void Export(){

    }


}
