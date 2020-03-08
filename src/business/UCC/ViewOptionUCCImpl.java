package business.UCC;

import exceptions.BizzException;
import javafx.scene.control.Alert;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class ViewOptionUCCImpl implements ViewOptionUCC{


    private String ContentTextExport = "this project does not exist on the path: ";


    /**
     * {@inheritDoc}
     */
    public void Export(File dir, File selectedFile) {
        try {
            if ( selectedFile != null ) {
                if (dir.exists()) {
                    createTarGz(dir.toString(), selectedFile.getAbsolutePath().concat(".tar.gz") );
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error Export " + ContentTextExport).showAndWait();
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
     */
    @Override
    public void createTarGz(String folderProject, String fileTarDestination) throws IOException, BizzException {
        File root = new File(folderProject);
        // create tar archive
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileTarDestination));
        GZIPOutputStream gzIpoutput = new GZIPOutputStream(new BufferedOutputStream(fileOutputStream));
        TarArchiveOutputStream archiveTarGz = new TarArchiveOutputStream(gzIpoutput);
        addFileToArchiveTarGz(folderProject, "", archiveTarGz);
        archiveTarGz.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        }catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Impossible to export the project").showAndWait();
        }


    }
}
