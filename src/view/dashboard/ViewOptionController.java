package view.dashboard;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import business.factories.UserFactoryImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.ProjectDAO;
import persistence.UserDAOImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class ViewOptionController extends HBox {

    private String rootProject = "/ProjectTikZ/";
    private String ContentTextExport = "this project does not exist on the path: ";

    @FXML
    private Label projectName = null;
    @FXML
    private Button exportBtn;
    @FXML
    private ImageView exportIcon = null;
    @FXML
    private HBox projectRowHbox = null;

    public ViewOptionController(UserDTO userDTO) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/dashboard/viewOption.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {

        exportBtn.setOnAction(event -> {
            UserFactoryImpl userFactory = new UserFactoryImpl();
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
            UserUCC userUcc = new UserUCCImpl(dal, dao);
            UserDTO user = userUcc.getConnectedUser();
            ArrayList<ProjectDTO> chooserProject = ProjectDAO.getInstance().getSelectedProject(user.getUser_id(), projectName.getText());

            FileChooser fc = new FileChooser();
            fc.setTitle("Save project as...");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
            fc.setInitialDirectory(new File(System.getProperty("user.home") + rootProject));
            fc.setInitialFileName(projectName.getText());
            File selectedFile = fc.showSaveDialog(null);
            File dir = new File(chooserProject.get(0).getProjectPath());
            Export(dir, selectedFile);
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

    public void Export(File dir, File selectedFile) {
        try {
            if (selectedFile != null) {
                if (dir.exists()) {
                    createTarGz(dir.toString(), selectedFile.getAbsolutePath().concat(".tar.gz"));
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error Export " + ContentTextExport).showAndWait();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTarGz(String folderProject, String fileTarDestination) throws IOException {
        File root = new File(folderProject);
        // create tar archive
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileTarDestination));
        GZIPOutputStream gzIpoutput = new GZIPOutputStream(new BufferedOutputStream(fileOutputStream));
        TarArchiveOutputStream archiveTarGz = new TarArchiveOutputStream(gzIpoutput);
        addFileToArchiveTarGz(folderProject, "", archiveTarGz);
        archiveTarGz.close();
    }

    public void addFileToArchiveTarGz(String folderProject, String parent, TarArchiveOutputStream archiveTarGz) {
        File file = new File(folderProject);
        String entryName = parent + file.getName();
        // add tar ArchiveEntry
        try {
            archiveTarGz.putArchiveEntry(new TarArchiveEntry(file, entryName));

            if (file.isFile()) {
                BufferedInputStream fileSelected = new BufferedInputStream(new FileInputStream(file));
                IOUtils.copy(fileSelected, archiveTarGz);  // copy file in archive
                archiveTarGz.closeArchiveEntry();
                fileSelected.close();
            } else if (file.isDirectory()) {
                archiveTarGz.closeArchiveEntry();
                for (File fileInSubFolder : file.listFiles()) {
                    addFileToArchiveTarGz(fileInSubFolder.getAbsolutePath(), entryName + File.separator, archiveTarGz);
                }
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Impossible to export your project").showAndWait();
        }
    }

}
