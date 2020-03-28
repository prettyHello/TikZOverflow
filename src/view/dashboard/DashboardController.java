package view.dashboard;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import business.UCC.ProjectUCCImpl;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import business.factories.ProjectFactory;
import business.factories.ProjectFactoryImpl;
import business.factories.UserFactoryImpl;
import exceptions.BizzException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import persistence.*;
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

    DashboardController dbc = this;

    private String popupMessage = "Please enter the name of your Project" ;
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    private String ContentTextImport = "impossible to import, this project already exists in: ";

    ProjectUCCImpl projectUCC ;

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
        DALServices dal = new DALServicesImpl();
        ProjectFactory projectFactory = new ProjectFactoryImpl();
        ProjectDAO dao = new ProjectDAOImpl(dal, projectFactory);
        this.projectUCC = new ProjectUCCImpl(dal, dao);
        projectList = new ListView<>();
    }

    public void handleProfileButton() {
        viewSwitcher.switchView(ViewName.PROFILE);
    }


    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    public void setUserProjectView(UserDTO user) {
        DALServices dal = new DALServicesImpl();
        ProjectFactory projectFactory = new ProjectFactoryImpl();
        ProjectDAO projectDAO = new ProjectDAOImpl(dal, projectFactory);

        this.user = user;
        userSetting.setText(user.getFirstName());
        ArrayList<ProjectDTO> listOfProject = projectDAO.getProjects(user.getUserId());
        projectObsList = FXCollections.observableArrayList(listOfProject);
        projectList.setItems(projectObsList);
    }



    public void handleDisconnectButton() {
        viewSwitcher.switchView(ViewName.LOGIN);
        UserFactoryImpl userFactory = new UserFactoryImpl();
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
        UserUCC userUcc = new UserUCCImpl(dal, dao);
        userUcc.deleteConnectedUser();
    }


    public void initialize() {
        itemList = FXCollections.observableArrayList();
        itemList.add("Your projects");
        itemList.add("Shared with you");
        optionList.setItems(itemList);

        UserFactoryImpl userFactory = new UserFactoryImpl();
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
        UserUCC userUcc = new UserUCCImpl(dal, dao);
        UserDTO user = userUcc.getConnectedUser();

        userSetting.setText(user.getFirstName());

        projectList.setCellFactory(cell -> new ListCell<ProjectDTO>() {
            @Override
            protected void updateItem(ProjectDTO item, boolean empty) {
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
                            String Dst = Utility.unTarFile(selectedFile, folderDestination);
//                            Utility.uncompressTarGZ(selectedFile, folderDestination.toFile());
//                            String Dst = "ssaas";

                            projectUCC.renameFolderProject(new File(folderDestination.toFile()+File.separator+ Dst), new File(folderDestination.toString() + File.separator + projectName));
                            ProjectDTO newProjectImport = projectUCC.getProjectDTO(projectName, folderDestination, user.getUserId());
                            projectObsList.add(newProjectImport);
                            this.projectUCC.createFromImport(newProjectImport);
                        } catch (IOException  e) {
                            throw new BizzException("Could not locate files to import");
                        }
                    }
                    else {
                        new Alert(Alert.AlertType.ERROR, ContentTextImport + folderDestination).showAndWait();
                        throw new BizzException("Existing Project");
                    }
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "please select a file with a \".tar.gz\" extension ").showAndWait();
            }
        }
    }


    public void delete(ProjectDTO data) {
        projectObsList.remove(data);
    }

    @FXML
    public void newProject() {
        String projectName = askProjectName();

        if (projectName != null && projectName.matches(Utility.ALLOWED_CHARACTERS_PATTERN)) {
            try {
                ProjectDTO newProject = new ProjectDTO();
                newProject.setProjectName(projectName);
                newProject.setCreateDate(Utility.getTimeStamp());

                projectUCC.createNewProject(projectName);
                toEditorView();
            } catch (BizzException e) {
                Utility.showAlert(Alert.AlertType.WARNING, "Creation impossible", "Duplicate project name", "A project with ths name already exists. Please specify another one");
            }
        } else {
            new Alert(Alert.AlertType.ERROR, ContentTextImport).showAndWait();
        }
    }

    private void toEditorView() {
        viewSwitcher.switchView(ViewName.EDITOR);
    }

    private String askProjectName() {
        Optional<String> projectName;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Project name");
        dialog.setHeaderText(popupMessage);
        dialog.setContentText("Name :");
        projectName = dialog.showAndWait();
        if (projectName.isPresent() && projectName.get().matches(Utility.ALLOWED_CHARACTERS_PATTERN)) {
            return projectName.get();
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid name").showAndWait();
        }

        return null;
    }
}
