package business.UCC;

import exceptions.BizzException;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.File;
import java.io.IOException;

public interface ViewOptionUCC {

    public void Export(File dir, File selectedFile);

    public void createTarGz(String folderProject, String fileTarDestination) throws IOException, BizzException;

    public void addFileToArchiveTarGz(String folderProject, String parent, TarArchiveOutputStream archiveTarGz);
}
