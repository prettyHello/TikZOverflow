package business.UCC;

import business.DTO.ProjectDTO;
import exceptions.BizzException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Controller that handles operations on projects
 */
public interface ProjectUCC {

    /**
     * Rename imported project with input provided by the user
     *
     * @param projectName    folder to be rename
     * @param NewProjectName new folder name
     */
    void renameFolderProject(File projectName, File NewProjectName);

    /**
     * retrieve information for a given project & user
     *
     * @param projectName       project name
     * @param folderDestination path to the project folder
     * @param userId            project owner
     * @return
     */
    ProjectDTO getProjectDTO(String projectName, Path folderDestination, int userId);

    /**
     * retrieve information for a given project id
     *
     * @param projet the DTO containing the id of the project
     * @return the associated DTO
     */
    ProjectDTO get(ProjectDTO projet);

    /**
     * Creates a new project on disk and stores its path in the database
     *
     * @param projectName the name of the project
     * @throws BizzException if the project already exists
     * @throws IOException   if IO operations fail
     */
    void createNewProject(String projectName) throws BizzException, IOException;

    /**
     * Creates a new project on disk and stores its path in the database from a specified import
     *
     * @param projectDTO the path to the archive to import
     * @throws BizzException if the project in the archive has a problem or if a similar project already exists
     * @throws IOException   if an error occurred during finding/opening the archive
     */
    void createFromImport(ProjectDTO projectDTO) throws BizzException, IOException;
}
