package view.dashboard;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;

import business.UCC.ProjectUCCImpl;
import exceptions.BizzException;

import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import business.factories.UserFactoryImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.ProjectDAO;
import persistence.UserDAOImpl;
import utilities.Utility;
import view.ViewName;
import view.ViewSwitcher;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class DashboardController {

    private String popupMessage = "Please enter the name of your Project" ;
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    private String ContentTextImport = "impossible to import, this project already exists in: ";

    ProjectUCCImpl projectUCC = new ProjectUCCImpl();

    private ViewSwitcher viewSwitcher;
    @FXML
    private  MenuItem userSetting;

    @FXML
    private  MenuItem newProject;

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



    public void handleDisconnectButton() {
        viewSwitcher.switchView(ViewName.LOGIN);
        UserFactoryImpl userFactory = new UserFactoryImpl();
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
        UserUCC userUcc = new UserUCCImpl(dal, dao);
        userUcc.deleteConnectedUser();
    }


    public void initialize(){
        itemList = FXCollections.observableArrayList();
        itemList.add("Your projects");
        itemList.add("Shared with you");
        optionList.setItems(itemList);

        UserFactoryImpl userFactory = new UserFactoryImpl();
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
        UserUCC userUcc = new UserUCCImpl(dal, dao);
        UserDTO user = userUcc.getConnectedUser();

        userSetting.setText(user.getFirst_name());

        projectList.setCellFactory(cell -> new ListCell<ProjectDTO>() {
            @Override
            protected void updateItem(ProjectDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new ViewOptionController(user).setProjectName(item.getProjectName()).setExportIcon("images/exportIcon.png").setEditIcon().getProjectRowHbox());
                }
            }
        });
    }

    /**
     * Untar a choose file to user home, show to the dashboard and save into the database
     * @throws BizzException
     */
    @FXML
    public void importd() throws BizzException {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            String extention = selectedFile.getName().substring(selectedFile.getName().length() - 7, selectedFile.getName().length());

            if(extention.equals(".tar.gz")){
                String projectName = projectUCC.setProjectName(popupMessage);

                if (projectName != null) {
                    Path folderDestination = Paths.get(System.getProperty("user.home") + rootProject +projectName +File.separator +"touch.txt");
                    Path folderDestination2 = Paths.get(System.getProperty("user.home") + rootProject +projectName );

                    if (!Files.exists(folderDestination.resolve(projectName))) {
                        String Dst = Utility.unTarFile(selectedFile, folderDestination2);
                        projectUCC.renameFolderProject(new File(folderDestination.toFile()+File.separator+ Dst), new File(folderDestination.toString() + File.separator + projectName));
                        ProjectDTO newProjectImport = projectUCC.getProjectDTO(projectName, folderDestination, user.getUser_id());
                        projectObsList.add(newProjectImport);
                        ProjectDAO.getInstance().saveProject(newProjectImport);
                    }
                    else {
                        new Alert(Alert.AlertType.ERROR, ContentTextImport + folderDestination).showAndWait();
                        throw new BizzException("Existing Project");
                    }
                }
            }else {
                new Alert(Alert.AlertType.WARNING, "please select a file with a \".tar.gz\" extention ").showAndWait();
            }
        }
    }

    @FXML
    public void newProject() {
        Optional<String> projectName;
        TextInputDialog enterProjectName = new TextInputDialog();
        enterProjectName.setTitle("Project name");
        enterProjectName.setHeaderText(popupMessage);
        enterProjectName.setContentText("Name :");
        projectName = enterProjectName.showAndWait();
        if (projectName.isPresent() ) {
            if (projectName.get().matches(Utility.ALLOWED_CHARACTERS_PATTERN ) ) {
                System.out.println(projectName.get());

            }else {
                new Alert(Alert.AlertType.ERROR, ContentTextImport).showAndWait();
            }
        }
    }

}
