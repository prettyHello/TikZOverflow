package business.UCC;

import exceptions.BizzException;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.File;
import java.io.IOException;

public interface ViewOptionUCC {

    /**
     * compress and export a selected project
     *
     * @param dir          path to the folder we need to compress
     * @param selectedFile path to destination of the compressed file
     */
    void Export(File dir, File selectedFile);

    /**
     * create an empty ".tar.gz" folder in which compressed files will be added
     *
     * @param folderProject      path to the folder we need to compress
     * @param fileTarDestination path to destination of the compressed file
     * @throws IOException
     * @throws BizzException
     */
    void createTarGz(String folderProject, String fileTarDestination) throws IOException, BizzException;

    /**
     * Add a file into an existing ".tar.gz" file
     *
     * @param folderProject file to be added
     * @param parent        parent folder of the file to be added
     * @param archiveTarGz  destination of the file
     */
    void addFileToArchiveTarGz(String folderProject, String parent, TarArchiveOutputStream archiveTarGz);
}
