package view.dashboard;

import business.DTO.ProjectDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import persistence.ProjectDAO;
import utilities.Utility;
import view.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class DashboardController {

    private ViewSwitcher viewSwitcher;

    @FXML
    private ListView<String> projectList;

    @FXML
    private ListView<String> optionList;

    @FXML
    public Button button;

    private ObservableList<String> itemList;



    public DashboardController(){
        projectList = new ListView<>();
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    public void printGarbage(){

    }

    public void initialize(){
        itemList = FXCollections.observableArrayList();

        itemList.add("project");
        itemList.add("project 2");
        itemList.add("project 3");

        projectList.setItems(itemList);

        itemList = FXCollections.observableArrayList();

        itemList.add("create new project");
        itemList.add("Your projects");
        itemList.add("Shared with you");

        optionList.setItems(itemList);

        optionList.setItems(itemList);
        projectList.setCellFactory(cell -> new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                } else {
                    setGraphic(new ViewOptionController().setProjectName(item).setExportIcon("view/images/exportIcon.png").getProjectRowHbox());
                }
            }
        });

    }


    @FXML
    public void importd(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".tar.gz", ".tar.gz"));
        File selectedFile= fc.showOpenDialog(null);
        if (selectedFile != null) {
            String projectName = selectedFile.getName().replace(".tar.gz", "");
            File newfoldel = new File(System.getProperty("user.home") + "/ProjectTikZ");
            Path folderDestination = Paths.get(newfoldel + "/") ;
            try {
                if ( ! Files.exists(folderDestination.resolve(projectName))) {
                    Files.createDirectories(folderDestination);
                    Utility.unTarFile(selectedFile, folderDestination);

                    ProjectDTO projectDTO =  new ProjectDTO();
                    String projectNameHash = null; //call function that will compute the hash
                    projectDTO.setProjectName(projectName)
                            .setProjectReference(projectNameHash)
                            .setProjectPath(folderDestination.toString()+"/"+projectName)
                            .setCreateDate(new Date())
                            .setModificationDate(new Date()); //syntatic sugar

                    ProjectDAO.getInstance().saveProject(projectDTO);
                    projectList.getItems().add(projectName);

                }else  {
                    PopupController.projectExists("Error import", "impossible to import, this project already exists in: "+ folderDestination);
                }
            }catch (IOException e) { e.printStackTrace(); }
        }

    }





}
