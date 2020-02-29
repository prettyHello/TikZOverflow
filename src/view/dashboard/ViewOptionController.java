package view.dashboard;

import business.DTO.ProjectDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import persistence.ProjectDAO;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class ViewOptionController extends HBox {

    private String rootProject = "/ProjectTikZ/";
    private String ContentTextExport = "this project does not exist on the path: ";

    @FXML
    private  Label projectName = null ;
    @FXML
    private Button exportBtn = null ;
    @FXML
    private ImageView exportIcon = null;
    @FXML
    private HBox projectRowHbox  = null;

    public ViewOptionController()  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/dashboard/viewOption.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void  initialize() {
        ProjectDTO AllProject = ProjectDAO.getInstance().getProject();

        exportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Save project as...");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("tar.gz", "*"));
                fc.setInitialDirectory(new File(System.getProperty("user.home") + rootProject));
                fc.setInitialFileName(projectName.getText() );
                File selectedFile= fc.showSaveDialog(null);
                int index = 0 ;
                index= AllProject.getProjectName().indexOf(projectName.getText()) ;
                File dir = new File( AllProject.getProjectPath().get(index) );
                Export(dir, selectedFile);
            }
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
        if ( selectedFile != null ) {
            try {
                if (dir.exists()) {
                    createTarGz(dir.toString(), selectedFile.getAbsolutePath().concat(".tar.gz") );
                } else {
                    DashboardController popup = new DashboardController();
                    popup.ifProjectExists(dir.toPath(), "Error Export", ContentTextExport);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void addFileToArchiveTarGz(String folderProject, String parent, TarArchiveOutputStream archiveTarGz) throws IOException {
        File file = new File(folderProject);
        String entryName = parent + file.getName();
        // add tar ArchiveEntry
        archiveTarGz.putArchiveEntry(new TarArchiveEntry(file, entryName));
        if(file.isFile()){
            BufferedInputStream fileSelected = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(fileSelected, archiveTarGz);  // copy file in archive
            archiveTarGz.closeArchiveEntry();
            fileSelected.close();
        }else
            if(file.isDirectory()){
            archiveTarGz.closeArchiveEntry();
            for(File fileInSubFolder : file.listFiles()){ addFileToArchiveTarGz(fileInSubFolder.getAbsolutePath(), entryName+File.separator, archiveTarGz); }
            }
    }

}
