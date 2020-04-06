package view.dashboard;

import config.ConfigurationSingleton;
import controller.ProjectImpl;
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
import model.DALServices;
import model.ProjectDAO;
import model.ProjectDAOImpl;
import utilities.Utility;
import utilities.exceptions.BizzException;
import view.ViewName;
import view.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

/**
 * This class handles the main screen of the program and allows the user to manage through their projects.
 */
public class DashboardController {

    UserFactory userFactory;
    DALServices dal; //TODO remove once the code was refactored and dal is not used in view anymore
    UserUCC userUcc;
    ProjectUCC projectUCC ;
    ProjectFactory projectFactory;

    DashboardController dbc = this;
    private boolean useAskedName;

    private String popupMessage = "Please enter the name of your Project";
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    private String ContentTextImport = "impossible to import, this project already exists in: ";

    private ViewSwitcher viewSwitcher;

    @FXML
    private MenuItem userSetting;

    @FXML
    private ListView<ProjectImpl> projectList;

    @FXML
    private HBox editView;

    @FXML
    private ListView<String> optionList;

    private ObservableList<String> itemList;

    private ObservableList<ProjectImpl> projectObsList;

    private UserDTO user;

    public DashboardController() {;
        projectList = new ListView<>();
        //load the configuration
        this.dal = ConfigurationSingleton.getDalServices(); //TODO remove once the code was refactored and dal is not used in view anymore
        this.userFactory = ConfigurationSingleton.getUserFactory();
        this.userUcc = ConfigurationSingleton.getUserUcc();
        this.projectUCC = ConfigurationSingleton.getProjectUCC();
        this.projectFactory = ConfigurationSingleton.getProjectFactory();
    }

    /**
     * Switch to the user's profile view.
     */
    public void handleProfileButton() {
        viewSwitcher.switchView(ViewName.PROFILE);
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
     * @param user
     */
    public void setUserProjectView(UserDTO user) {
        ProjectDAO projectDAO = new ProjectDAOImpl(dal, projectFactory); //TODO remove once error below is fixed

        this.user = user;
        userSetting.setText(user.getFirstName());
        ArrayList<ProjectImpl> listOfProject = projectDAO.getOwnedProjects(user); //TODO this should be in a real controller, can't call model from view
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

        UserDTO user = userUcc.getConnectedUser();

        itemList = FXCollections.observableArrayList();
        itemList.add("Your projects");
        itemList.add("Shared with you");
        optionList.setItems(itemList);

        userSetting.setText(user.getFirstName());
        rootProject = File.separator + "ProjectTikZ" + File.separator +"userid_"+ user.getUserId() + File.separator;


        projectList.setCellFactory(cell -> new ListCell<ProjectImpl>() {
            @Override
            protected void updateItem(ProjectImpl item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ViewOptionController viewOptionController = new ViewOptionController(dbc, user, item.getProjectId());
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
     * @throws BizzException
     */
    //TODO file in models, some stuff elwhere blabla
    @FXML
    public void ImportProject() throws BizzException {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            String extension = selectedFile.getName().substring(selectedFile.getName().length() - 7, selectedFile.getName().length());

            if (extension.equals(".tar.gz")) {
                String projectName = askProjectName();

                if (projectName != null) {
                    Path folderDestination = Paths.get(System.getProperty("user.home") + rootProject);

                    if (!Files.exists(folderDestination.resolve(projectName))) {
                        try {
                            Files.createDirectories(folderDestination);
                            Files.createDirectories(folderDestination.resolve("tmp"));


                            String Dst = Utility.unTarFile(selectedFile, folderDestination.resolve("tmp"));

                            Files.createDirectories(folderDestination) ;
                            Files.createDirectories(folderDestination.resolve(projectName)) ;


                            Utility.copy(folderDestination.resolve("tmp"+ File.separator+ Dst).toFile(), folderDestination.resolve(projectName).toFile() );


                            projectUCC.renameFolderProject(new File(folderDestination.toFile()+File.separator+ File.separator+Dst), new File(folderDestination.toString() + File.separator + File.separator+projectName));

                            projectUCC.renameFolderProject(new File(folderDestination.toFile()+File.separator+ File.separator+projectName+File.separator+Dst+".bin"), new File(folderDestination.toFile()+File.separator+ File.separator+projectName+File.separator+projectName+".bin"));


                            ProjectImpl newProjectImport = this.getProjectDTO(projectName, folderDestination, user.getUserId());
                            projectObsList.add(newProjectImport);

                            Path delFile =   Paths.get(folderDestination.resolve("tmp"+File.separator).toString()) ;

                            System.out.println(delFile);

                            Utility.deleteFile(delFile.toFile());
                            this.createFromImport(newProjectImport);
                            setUserProjectView(this.user);
                        } catch (IOException e) {
                            throw new BizzException("Could not locate files to import");
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, ContentTextImport + folderDestination).showAndWait();
                        throw new BizzException("Existing Project");
                    }
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "please select a file with a \".tar.gz\" extension ").showAndWait();
            }
        }
    }
    //TODO some shit that was in the controller
    private ProjectImpl getProjectDTO(String projectName, Path folderDestination, int userId) {
        return new ProjectImpl().
                setProjectOwnerId(userId)
                .setProjectName(projectName)
                .setProjectPath(folderDestination.toString()+ File.separator +projectName)
                .setCreateDate(Utility.getTimeStamp())
                .setModificationDate(Utility.getTimeStamp());
    }
    private void createFromImport(ProjectImpl projectDTO) throws BizzException, IOException {
        ConfigurationSingleton.getProjectDAO().create(projectDTO);
    }

    /**
     * Delete an existing project.
     *
     * @param data
     */
    public void delete(ProjectImpl data) {
        projectObsList.remove(data);
    }

    /**
     * Create a new project, requiring the name and checking that it doesn't exist a project with the same name.
     */
    @FXML
    public void newProject() {
        this.useAskedName = false;
        String projectName = askProjectName();
        if (!this.useAskedName) {
            return;
        }

        if (projectName != null && projectName.matches(Utility.ALLOWED_CHARACTERS_PATTERN)) {
            try {
                ProjectImpl newProject = new ProjectImpl();
                newProject.setProjectName(projectName);
                newProject.setCreateDate(Utility.getTimeStamp());

                projectUCC.create(projectName);
                toEditorView();
            } catch (BizzException e) {
                Utility.showAlert(Alert.AlertType.WARNING, "Creation impossible", "Duplicate project name", "A project with ths name already exists. Please specify another one");
            } catch (IOException e) {
                //TODO I/O exception
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, ContentTextImport).showAndWait();
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
                new Alert(Alert.AlertType.ERROR, "Please enter a valid name").showAndWait();
            }
        }

        return null;
    }

    /**
     * Change actual view to project editor view.
     */
    private void toEditorView() {
        viewSwitcher.switchView(ViewName.EDITOR);
    }
}
