package view.dashboard;

import business.DTO.ProjectDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import java.util.Optional;

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

        ProjectDTO AllProject = ProjectDAO.getInstance().getProject();

        itemList = FXCollections.observableArrayList();

        for (String s : AllProject.getProjectName()) {
            itemList.add(s);

        }


        projectList.setItems(itemList);

        itemList = FXCollections.observableArrayList();

        itemList.add("create new project");
        itemList.add("Your projects");
        itemList.add("Shared with you");

        optionList.setItems(itemList);

        optionList.setItems(itemList);
        projectList.setCellFactory(cell -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){ setGraphic(null); } else {
                    setGraphic(new ViewOptionController().setProjectName(item).setExportIcon("view/images/exportIcon.png").getProjectRowHbox()); } } });
    }


    @FXML
    public void importd(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".tar.gz", ".tar.gz"));
        File selectedFile= fc.showOpenDialog(null);
        String popupMessage = "Please enter the name of your Project" ;
        String projectName = setProjectName(popupMessage);
        Path folderDestination = Paths.get(System.getProperty("user.home") + "/ProjectTikZ/") ;
        String folderNameUntar = selectedFile.getName().replace(".tar.gz", "");

        if (selectedFile != null) {

            try {
                if ( ! Files.exists(folderDestination.resolve(projectName))) {
                    Files.createDirectories(folderDestination);
                    Utility.unTarFile(selectedFile, folderDestination);
                    renameFolderProject( new File(folderDestination.resolve(folderNameUntar).toString()), new File(folderDestination.toString() +"/"+ projectName) );
                    ProjectDTO projectDTO =  new ProjectDTO();
                    String projectNameHash = null; //call function that will compute the hash
                    saveProjectInDB (projectDTO, projectName, projectNameHash, folderDestination);
                    projectList.getItems().add(projectName);
                }else  {
                     ifProjectExists(folderDestination) ;
                }
            }catch (IOException e) { e.printStackTrace(); }
        }

    }


    public void ifProjectExists(Path folderDestination) {
        Alert alertMessage = new Alert(Alert.AlertType.NONE);
        alertMessage.setAlertType(Alert.AlertType.INFORMATION);
        alertMessage.setTitle("Error import");
        alertMessage.setContentText("impossible to import, this project already exists in: "+ folderDestination);
        alertMessage.show();
    }

    public void renameFolderProject(File projectName, File NewProjectName){
        projectName.renameTo(NewProjectName);
    }

    public String setProjectName (String popupMessage){
        TextInputDialog enterProjectName = new TextInputDialog();
        enterProjectName.setTitle("Project name");
        enterProjectName.setHeaderText(popupMessage);
        enterProjectName.setContentText("Name :");
        Optional<String> projectName = enterProjectName.showAndWait();

        if (projectName.isPresent()) return projectName.get() ;
       return null;
    }

    public void saveProjectInDB (ProjectDTO projectDTO,String projectName,String projectNameHash ,Path folderDestination){
        projectDTO.setProjectName(projectName)
                .setProjectReference(projectNameHash)
                .setProjectPath(folderDestination.toString()+"/"+projectName)
                .setCreateDate(Utility.getTimeStamp())
                .setModificationDate(Utility.getTimeStamp()); //syntatic sugar

        ProjectDAO.getInstance().saveProject(projectDTO);
    }





}
