package view.dashboard;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import exceptions.BizzException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import persistence.ProjectDAO;
import utilities.Utility;
import view.ViewName;
import view.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class DashboardController {

    private String popupMessage = "Please enter the name of your Project" ;
    private String rootProject = "/ProjectTikZ/";
    private String ContentTextImport = "impossible to import, this project already exists in: ";

    private ViewSwitcher viewSwitcher;
    @FXML
    private  MenuItem userSetting;

    @FXML
    private ListView<ProjectDTO> projectList;

    @FXML
    private ListView<String> optionList;

    private ObservableList<String> itemList;

    private ObservableList<ProjectDTO> projectObsList;

    private UserDTO user;

    public DashboardController() {
        projectList = new ListView<>();
    }

    public void handleProfileButton() {

        viewSwitcher.switchView(ViewName.PROFILE);
    }

    public void handleDisconnectButton(){
        viewSwitcher.switchView(ViewName.LOGIN);

    }

    public DashboardController setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
        return  this;
    }

    public DashboardController setUserProjectView(UserDTO user) {
        this.user = user;
        userSetting.setText(user.getFirst_name());
        ArrayList<ProjectDTO>  listOfProject = ProjectDAO.getInstance().getProjects(user.getUser_id());
        projectObsList = FXCollections.observableArrayList(listOfProject);
        projectList.setItems(projectObsList);

        return  this;
    }

    public void initialize(){

        itemList = FXCollections.observableArrayList();
        itemList.add("create new project");
        itemList.add("Your projects");
        itemList.add("Shared with you");
        optionList.setItems(itemList);

        projectList.setCellFactory(cell -> new ListCell<ProjectDTO>() {
            @Override
            protected void updateItem(ProjectDTO item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){ setGraphic(null); } else {
                    setGraphic(new ViewOptionController(user).setProjectName(item.getProjectName()).setExportIcon("view/images/exportIcon.png").getProjectRowHbox()); } } });
    }

    @FXML
    public void importd() throws BizzException{
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".tar.gz", ".tar.gz"));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            // lever une exception pour le type de caractères a saisir concernant le nom du dossier contenant le projet
            // recuperer dans utility les exceptions permettant de controller

            String projectName = setProjectName(popupMessage);

            if (projectName.isEmpty() || !(projectName.matches(Utility.ALLOWED_CHARACTERS_PATTERN))){

                throw new BizzException("Unallowed characters OR Empty Name");
            }
            else {
                Path folderDestination = Paths.get(System.getProperty("user.home") + rootProject);
                String folderNameUnbar = selectedFile.getName().replace(".tar.gz", "");

                if (!Files.exists(folderDestination.resolve(projectName))) {
                    try {
                        Files.createDirectories(folderDestination);
                        Utility.unTarFile(selectedFile, folderDestination);

                        renameFolderProject(new File(folderDestination.resolve(folderNameUnbar).toString()), new File(folderDestination.toString() + "/" + projectName));
                        String projectNameHash = null; //call function that will compute the hash
                        ProjectDTO newProjectImport = getProjectDTO(projectName, folderDestination, user.getUser_id());
                        projectObsList.add(newProjectImport);
                        ProjectDAO.getInstance().saveProject(newProjectImport);
                    } catch (BizzException | IOException e) {
                        e.getMessage();
                    }
                } else {
                    throw new BizzException("Existing Project");
                }
            }
        }
    }


    private ProjectDTO getProjectDTO(String projectName, Path folderDestination, int userId) {
        return  new ProjectDTO().
                setProjectOwnerId(userId)
                .setProjectName(projectName)
                .setProjectPath(folderDestination.toString()+"/"+projectName)
                .setCreateDate(Utility.getTimeStamp())
                .setModificationDate(Utility.getTimeStamp());
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
        if (projectName.isPresent()){
            return projectName.get();}
        return null;
    }
}
