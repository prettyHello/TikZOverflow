package view.dashboard;

import config.ConfigurationSingleton;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.UCC.ProjectUCC;
import controller.UCC.UserUCC;
import controller.factories.ProjectFactory;
import controller.factories.UserFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import model.ProjectDAO;
import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;
import view.ViewName;
import view.ViewSwitcher;

import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Optional;

import static utilities.Utility.showAlert;

/**
 * This class handles the main screen of the program and allows the user to manage through their projects.
 */
public class DashboardController {

    UserFactory userFactory = ConfigurationSingleton.getUserFactory();
    UserUCC userUcc = ConfigurationSingleton.getUserUcc();
    ProjectUCC projectUCC = ConfigurationSingleton.getProjectUCC();
    ProjectFactory projectFactory = ConfigurationSingleton.getProjectFactory();
    ProjectDAO projectDAO = ConfigurationSingleton.getProjectDAO();

    DashboardController dbc = this;
    private boolean useAskedName;

    private String popupMessage = "Please enter the name of your Project";

    private ViewSwitcher viewSwitcher;

    @FXML
    private MenuItem userSetting;

    @FXML
    private ListView<ProjectDTO> projectList;

    @FXML
    private HBox editView;

    @FXML
    private ListView<String> optionList;

    private ObservableList<String> itemList;

    private ObservableList<ProjectDTO> projectObsList;

    private UserDTO user;

    public DashboardController() {
    }

    /**
     * Switch to the user's profile view.
     */
    public void handleProfileButton() {
        this.viewSwitcher.switchView(ViewName.PROFILE);
    }

    /**
     * Required to load view.
     *
     * @param viewSwitcher
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    /**
     * Set the projects of the current user.
     *
     * @param userDto
     */
    public void setUserProjectView(UserDTO userDto) {
        this.user = userDto;
        userSetting.setText(user.getFirstName());
        ArrayList<ProjectDTO> listOfProject = this.projectUCC.getOwnedProjects(userDto);
        projectObsList = FXCollections.observableArrayList(listOfProject);
        projectList.setItems(projectObsList);
    }

    /**
     * Disconnect current user from the program and go back to login screen.
     */
    public void handleDisconnectButton() {
        viewSwitcher.switchView(ViewName.LOGIN);
        userUcc.deleteConnectedUser();
    }

    public void initialize() {
        UserDTO userDto = userUcc.getConnectedUser();
        itemList = FXCollections.observableArrayList();
        itemList.add("Your projects");
        itemList.add("Shared with you");
        optionList.setItems(itemList);
        userSetting.setText(userDto.getFirstName());

        projectList.setCellFactory(cell -> new ListCell<ProjectDTO>() {
            @Override
            protected void updateItem(ProjectDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ViewOptionController viewOptionController = new ViewOptionController(dbc, userDto, item);
                    viewOptionController.setProject(item);
                    viewOptionController.setExportIcon();
                    viewOptionController.setEditIcon();
                    viewOptionController.setDeleteIcon();
                    viewOptionController.setViewSwitcher(viewSwitcher);
                    setGraphic(viewOptionController.getProjectRowHbox());
                }
            }
        });
    }

    /**
     * Decompress a choose file to user home, display it on the dashboard and save it into the database
     * Untar a choose file to user home, show to the dashboard and save into the database
     *
     */
    @FXML
    public void ImportProject(){
        FileChooser fc = new FileChooser();
        ProjectDTO projectDTO = null;
        //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
        File selectedFile = fc.showOpenDialog(null);
        String projectName = askProjectName();
        if(selectedFile == null)
            return;
        if(projectName == null)
            return;
        try{
            projectDTO = this.projectFactory.createProject(projectName);
            projectDTO = this.projectUCC.load(selectedFile,projectDTO);
        }catch (BizzException e){
            showAlert(Alert.AlertType.WARNING, "Load", "Business Error", e.getMessage());
        }catch (FatalException e){
            showAlert(Alert.AlertType.WARNING, "Load", "Unexpected Error", e.getMessage());
        }
        this.projectObsList.add(projectDTO);
        setUserProjectView(this.user);
    }

    /**
     * Delete an existing project.
     *
     * @param dto
     */
    public void delete(ProjectDTO dto) {
        projectObsList.remove(dto);
        setUserProjectView(this.user);
    }

    /**
     * Create a new project, requiring the name and checking that it doesn't exist a project with the same name.
     */
    @FXML
    public void newProject() {
        this.useAskedName = false;
        String projectName = askProjectName();
        if(projectName == null) // askProjectName return null if user cancelled the action
            return;

        try {
            ProjectDTO projectDto = this.projectFactory.createProject();
            projectDto.setProjectName(projectName);
            projectUCC.create(projectDto);
            this.viewSwitcher.switchView(ViewName.EDITOR);
        } catch (BizzException e){
            showAlert(Alert.AlertType.WARNING, "newProject", "Business Error", e.getMessage());
        }catch (FatalException e){
            showAlert(Alert.AlertType.WARNING, "newProject", "Unexpected Error", e.getMessage());
        }
    }

    /**
     * Ask the name of the new project.
     *
     * @return a String with the new name or null
     */
    private String askProjectName() {
        Optional<String> projectName;
        TextInputDialog dialog = new TextInputDialog();

        final Button confirm = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        confirm.addEventFilter(ActionEvent.ACTION, event -> this.useAskedName = true);
        dialog.setTitle("Project name");
        dialog.setHeaderText(popupMessage);
        dialog.setContentText("Name :");
        projectName = dialog.showAndWait();

        if (projectName.isPresent() && projectName.get().matches(Utility.ALLOWED_CHARACTERS_PATTERN)) {
            return projectName.get();
        } else {
            if (this.useAskedName) {
                showAlert(Alert.AlertType.WARNING, "newProject", "Please enter a valid name", "Please enter a valid name");
            }
        }
        return null;
    }
}
