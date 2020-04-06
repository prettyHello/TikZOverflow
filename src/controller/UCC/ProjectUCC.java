package controller.UCC;

import controller.DTO.ProjectDTO;
import controller.ProjectImpl;
import utilities.exceptions.BizzException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Use Case Controller that handles operations on projects
 */
public interface ProjectUCC {

    /**
     * Rename imported project with input provided by the user
     *
     * @param projectName    folder to be rename
     * @param NewProjectName new folder name
     */
    //TODO MoveToDAO
    void renameFolderProject(File projectName, File NewProjectName);


    /**
     * Creates a new project on disk and stores its path in the database
     *
     * @param dto containing the projectName the name of the project
     * @throws BizzException if the project already exists
     * @throws IOException   if IO operations fail
     */
    void create(ProjectDTO dto) throws BizzException, IOException;


}
