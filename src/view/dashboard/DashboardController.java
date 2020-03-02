package view.dashboard;

<<<<<<< HEAD
import business.DTO.ProjectDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import persistence.ProjectDAO;
import utilities.Utility;
=======
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import view.ViewName;
>>>>>>> 8755af8b21b625e66ee37bcd6010a0a41d9fa7c0
import view.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class DashboardController {
<<<<<<< HEAD

    private String popupMessage = "Please enter the name of your Project" ;
    private String rootProject = "/ProjectTikZ/";
    private String ContentTextImport = "impossible to import, this project already exists in: ";


    private ViewSwitcher viewSwitcher;

=======
>>>>>>> 8755af8b21b625e66ee37bcd6010a0a41d9fa7c0
    @FXML
    public Button bt_disconnect;

    @FXML
    public Button bt_profile;

<<<<<<< HEAD
    public DashboardController(){
        projectList = new ListView<>();
=======
    ViewSwitcher viewSwitcher;

    public void handleProfileButton(){viewSwitcher.switchView(ViewName.PROFILE);}

    public void handleDisconnectButton(){
        viewSwitcher.switchView(ViewName.LOGIN);
>>>>>>> 8755af8b21b625e66ee37bcd6010a0a41d9fa7c0
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
<<<<<<< HEAD

    public void printGarbage(){

    }

    public void initialize(){

        ProjectDTO AllProject = ProjectDAO.getInstance().getProject();

        itemList = FXCollections.observableArrayList();

        for (String s : AllProject.getProjectName()) { itemList.add(s); }

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
    public void importd() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".tar.gz", ".tar.gz"));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            String projectName = setProjectName(popupMessage);

            if (projectName != null){
                Path folderDestination = Paths.get(System.getProperty("user.home") + rootProject);
                String folderNameUnbar = selectedFile.getName().replace(".tar.gz", "");

                if (!Files.exists(folderDestination.resolve(projectName))) {
                    try {
                        Files.createDirectories(folderDestination);
                    } catch (IOException e) {
                        System.out.println("EXXePTION CODE");
                        e.printStackTrace();
                    }
                    Utility.unTarFile(selectedFile, folderDestination);
                    renameFolderProject(new File(folderDestination.resolve(folderNameUnbar).toString()), new File(folderDestination.toString() + "/" + projectName));
                    ProjectDTO projectDTO = new ProjectDTO();
                    String projectNameHash = null; //call function that will compute the hash
                    saveProjectInDB(projectDTO, projectName, projectNameHash, folderDestination);
                    projectList.getItems().add(projectName);
                } else {
                    ifProjectExists(folderDestination, "Error import", ContentTextImport);
                }
            }
        }
    }


    public void ifProjectExists(Path folderDestination, String title, String ContentText) {
        Alert alertMessage = new Alert(Alert.AlertType.NONE);
        alertMessage.setAlertType(Alert.AlertType.INFORMATION);
        alertMessage.setTitle(title);
        alertMessage.setContentText(ContentText + folderDestination);
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

        if (projectName.isPresent()){ return projectName.get();}
        return null;

        //return projectName.orElseGet(projectName::get);
    }

    public void saveProjectInDB (ProjectDTO projectDTO,String projectName,String projectNameHash ,Path folderDestination){
        projectDTO.setProjectName(projectName)
                .setProjectReference(projectNameHash)
                .setProjectPath(folderDestination.toString()+"/"+projectName)
                .setCreateDate(Utility.getTimeStamp())
                .setModificationDate(Utility.getTimeStamp()); //syntatic sugar

        ProjectDAO.getInstance().saveProject(projectDTO);
    }





=======
>>>>>>> 8755af8b21b625e66ee37bcd6010a0a41d9fa7c0
}
