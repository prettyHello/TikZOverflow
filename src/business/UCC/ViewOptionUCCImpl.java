package business.UCC;

import exceptions.BizzException;
import javafx.scene.control.Alert;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class ViewOptionUCCImpl implements ViewOptionUCC{


    private String ContentTextExport = "the project does not exist on the path: ";


    /**
     * {@inheritDoc}
     */
    public void ExportProject(File dir, File selectedFile) {
        try {
            if ( selectedFile != null ) {
                if (dir.exists()) {
                    if ( createTarGz(dir.toString(), selectedFile.getAbsolutePath().concat(".tar.gz") ) ) {
                        new Alert(Alert.AlertType.CONFIRMATION, "File exported to : " + selectedFile.getAbsolutePath().concat(".tar.gz")).showAndWait();
                    }
                    else {
                        new Alert(Alert.AlertType.ERROR, "Too long path to a certain file ( > 100 bytes)").showAndWait();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error Export " + ContentTextExport +dir ).showAndWait();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        catch (BizzException e){
            e.getMessage();
        }
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Boolean createTarGz(String folderProject, String fileTarDestination) throws IOException, BizzException {
        File root = new File(folderProject);
        // create tar archive
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileTarDestination));
        GZIPOutputStream gzIpoutput = new GZIPOutputStream(new BufferedOutputStream(fileOutputStream));
        TarArchiveOutputStream archiveTarGz = new TarArchiveOutputStream(gzIpoutput);

        try
        {
            addFileToArchiveTarGz(folderProject, "", archiveTarGz);
            return true;
        }
        catch ( RuntimeException e) {
        return false ;
        }
        finally {
            archiveTarGz.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFileToArchiveTarGz(String folderProject, String parent, TarArchiveOutputStream archiveTarGz){
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
                    addFileToArchiveTarGz(fileInSubFolder.getAbsolutePath(), entryName + "/", archiveTarGz);
                }
            }
        }catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Impossible to export the project").showAndWait();
        }


    }
}
