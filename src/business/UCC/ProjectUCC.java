package business.UCC;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import exceptions.BizzException;
import exceptions.FatalException;

import java.io.File;
import java.nio.file.Path;

public interface ProjectUCC {
    /**
     * Rename imported project with input provided by the user
     * @param projectName  folder to be rename
     * @param NewProjectName new folder name
     */

    public void renameFolderProject(File projectName, File NewProjectName);

    /**
     * this setter retrieves the project name from the user thank to the dialog box
     * @param popupMessage  this variable contents the instruction showed to the user
     * @return return the project name typed by the user
     */

    public String setProjectName (String popupMessage) throws BizzException;

    /**
     *  retrieve information for a given project & user
     * @param projectName  project name
     * @param folderDestination path to the project folder
     * @param userId  project owner
     * @return
     */

    public ProjectDTO getProjectDTO(String projectName, Path folderDestination, int userId);

    }
