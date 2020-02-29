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
        exportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                File selectedFile= fc.showSaveDialog(null);
                ProjectDTO queryProject = ProjectDAO.getInstance().getProjectPath(projectName.getText());
                try {
                    createTarGz(queryProject.getProjectPath().toString()) ;
                    

                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    public void Export(String folderSource) throws IOException{
        createTarGz(folderSource);

    }

    public void createTarGz(String folderProject) throws IOException {
        File root = new File(folderProject);
        // create tar archive
        String fileTarDestination = System.getProperty("user.home").concat("/ProjectTikZ/") + "archive.tar.gz";
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
