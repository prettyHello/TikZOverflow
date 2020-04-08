package controller.UCC;

import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.File;
import java.util.ArrayList;

/**
 * Use Case Controller that handles operations on projects
 */
public interface ProjectUCC {

    /**
     * Creates a new project on disk and stores its path in the database
     *
     * @param dto containing the projectName
     * @throws BizzException
     * @throws FatalException
     */
    void create(ProjectDTO dto) throws BizzException, FatalException;

    /**
     * compress and export a selected project
     * @param dto use the id in dto to get the values through the model
     * @param selectedFile the selected file
     */
    void export(File selectedFile, ProjectDTO dto) throws FatalException;

    /**
     * Import the selected file as a project
     * @param selectedFile
     * @throws FatalException
     */
    ProjectDTO load(File selectedFile, ProjectDTO projectDto) throws FatalException;

    /**
     * Delete the given project
     * @param dto with the ownerId and the projectName
     * @throws FatalException
     */
    void delete(ProjectDTO dto) throws FatalException;

    /**
     * Save the ActiveCanvas
     * @throws FatalException
     */
    void save() throws FatalException;

    Canvas loadSavedCanvas()throws FatalException;

    void setActive(ProjectDTO dto) throws FatalException;

    ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException;
}
