package model;

import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import utilities.exceptions.FatalException;

import java.io.File;
import java.util.ArrayList;

/**
 * Interface for database action regarding projects
 */
public interface ProjectDAO extends DAO<ProjectDTO> {

    /**
     * Retrieve a project from the database
     *
     * @param dto In the UserDTO,
     *            the id  will be used to retrieve the list of projects that the user own
     * @return an array list of project DTO related to that owner
     */
    ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException;

    /**
     * compress and export a selected project
     * @param dto use the id in dto to get the values through the model
     */
    void export(File selectedFile, ProjectDTO dto) throws FatalException;

    void save(Canvas canvas, UserDTO userDto) throws FatalException;

    Canvas loadSavedCanvas(UserDTO userDto) throws FatalException;

    /**
     * Rename imported project with input provided by the user
     *
     * @param projectName    folder to be rename
     * @param NewProjectName new folder name
     */
    void renameFolderProject(File projectName, File NewProjectName);

    ProjectDTO load(File selectedFile, ProjectDTO projectDto) throws FatalException ;
}
