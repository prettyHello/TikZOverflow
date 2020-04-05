package controller.UCC;

import controller.DTO.ProjectDTO;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import utilities.exceptions.BizzException;
import view.dashboard.DashboardController;

import java.io.File;
import java.io.IOException;

//TODO LES FAMEUX USECASE THE VEIWOPTION, TU FAIS UNE VIEWOPTION, ILS FONT DES VIEWOPTIONS....
//TODO MERGE AVEC PROJECT...
public interface ViewOptionUCC {

    /**
     * compress and export a selected project
     *
     * @param dir          path to the folder we need to compress
     * @param selectedFile path to destination of the compressed file
     */
    public void ExportProject(File dir, File selectedFile);

    /**
     * create an empty ".tar.gz" folder in which compressed files will be added
     *
     * @param folderProject      path to the folder we need to compress
     * @param fileTarDestination path to destination of the compressed file
     * @throws IOException
     * @throws BizzException
     * @return
     */
    //TODO UTILS ?
    public Boolean createTarGz(String folderProject, String fileTarDestination) throws IOException, BizzException;

    /**
     * Add a file into an existing ".tar.gz" file
     *
     * @param folderProject file to be added
     * @param parent        parent folder of the file to be added
     * @param archiveTarGz  destination of the file
     */
    //TODO UTILS ?
    public void addFileToArchiveTarGz(String folderProject, String parent, TarArchiveOutputStream archiveTarGz) throws RuntimeException ;

    //TODO BIEN SUR, DANS UN AUTRE USECASECONTROLLER, C'EST EVIDENT
    public void deleteProject (ProjectDTO project, DashboardController dashboard ) ;
}
