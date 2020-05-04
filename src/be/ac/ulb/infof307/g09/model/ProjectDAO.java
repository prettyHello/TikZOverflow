package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

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

    /**
     * Save the canvas into a .bin file
     * @param canvas the canvas that needs to be saved
     * @param dto the project concerned
     * @throws FatalException in case of I/O exception
     */
    void save(Canvas canvas, ProjectDTO dto) throws FatalException;

    /**
     * Load a canvas previously saved into a .bin file
     * If the .bin file has been deleted or never saved, a new Canvas is created
     * @param dto of project concerned
     * @return the Canvas
     * @throws FatalException in case of I/O exception
     */
    Canvas loadSavedCanvas(ProjectDTO dto, String password) throws FatalException;

    /**
     * Load a project previously exported in a tar.gz
     *
     * @param selectedArchive The archive to import
     * @param projectDTO      DTO with the name of the new project once imported
     * @param userDTO         Active user
     * @return the projectDTO of the newly loaded project
     * @throws FatalException in case of I/O Exception or if a project of that name already exists
     * @throws BizzException  in case of invalid or empty tar.gz
     */
    ProjectDTO load(File selectedArchive, ProjectDTO projectDTO, UserDTO userDTO) throws FatalException, BizzException;
}
