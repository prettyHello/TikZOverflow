package be.ac.ulb.infof307.g09.view.dashboard;


import be.ac.ulb.infof307.g09.config.ConfigurationHolder;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.UCC.ProjectUCC;
import be.ac.ulb.infof307.g09.controller.UCC.UserUCC;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.view.ViewUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import be.ac.ulb.infof307.g09.view.ViewName;
import be.ac.ulb.infof307.g09.view.ViewSwitcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import static be.ac.ulb.infof307.g09.view.ViewUtility.showAlert;

/**
 * This class handles the main screen of the program and allows the user to manage through their projects.
 */
public class DashboardController {
    final UserUCC userUcc = ConfigurationHolder.getUserUcc();
    final ProjectUCC projectUCC = ConfigurationHolder.getProjectUCC();
    final ProjectFactory projectFactory = ConfigurationHolder.getProjectFactory();

    private final DashboardController dbc = this;
    private boolean useAskedName;

    private ViewSwitcher viewSwitcher;

    @FXML
    private MenuItem userSetting;
    @FXML
    private ListView<ProjectDTO> projectList;
    @FXML
    private ListView<String> optionList;

    private ObservableList<String> itemList;

    private ObservableList<ProjectDTO> projectObsList;

    private UserDTO user;

    public DashboardController() {
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
                    ProjectItemView viewOptionController = new ProjectItemView(dbc, userDto, item);
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
     * Switch to the user's profile be.ac.ulb.infof307.g09.view.
     */
    public void handleProfileButton() {
        this.viewSwitcher.switchView(ViewName.PROFILE);
    }

    /**
     * Required to load be.ac.ulb.infof307.g09.view.
     *
     * @param viewSwitcher the object responsible for the changing of be.ac.ulb.infof307.g09.view in the application
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    /**
     * Set the projects of the current user.
     *
     * @param userDto contains the data about the user
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

    /**
     * Decompress a choose file to user home, display it on the dashboard and save it into the database
     * Untar a choose file to user home, show to the dashboard and save into the database
     */
    @FXML
    public void importProject() {
        FileChooser fc = new FileChooser();
        ProjectDTO projectDTO = null;
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile == null) return;
        String password = ViewUtility.askProjectPassword();
        if(password==null)return;
        String projectName = askProjectName();
        if (projectName == null) return;
        try {
            projectDTO = this.projectFactory.createProject(projectName);
            projectDTO.setProjectPassword(password);
            projectDTO = this.projectUCC.load(selectedFile, projectDTO);
        } catch (BizzException e) {
            showAlert(Alert.AlertType.WARNING, "Load", "Business Error", e.getMessage());
        } catch (FatalException e) {
            showAlert(Alert.AlertType.WARNING, "Load", "Unexpected Error", e.getMessage());
        }
        this.projectObsList.add(projectDTO);
        setUserProjectView(this.user);
    }

    /**
     * Delete an existing project.
     *
     * @param dto contains the information about the project
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
        if (projectName == null) {// askProjectName return null if user cancelled the action
            return;
        }
        try {
            ProjectDTO projectDto = this.projectFactory.createProject();
            projectDto.setProjectName(projectName);
            projectUCC.create(projectDto);
            this.viewSwitcher.switchView(ViewName.EDITOR);
        } catch (BizzException e) {
            showAlert(Alert.AlertType.WARNING, "newProject", "Business Error", e.getMessage());
        } catch (FatalException e) {
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
        String popupMessage = "Please enter the name of your Project";
        dialog.setHeaderText(popupMessage);
        dialog.setContentText("Name :");
        projectName = dialog.showAndWait();

        if (projectName.isPresent() && projectName.get().matches(ViewUtility.ALLOWED_CHARACTERS_PATTERN)) {
            return projectName.get();
        } else {
            if (this.useAskedName) {
                showAlert(Alert.AlertType.WARNING, "newProject", "Please enter a valid name", "Please enter a valid name");
            }
        }
        return null;
    }

}
