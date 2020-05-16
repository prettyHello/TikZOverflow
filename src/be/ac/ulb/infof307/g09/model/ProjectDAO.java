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
     * No specific encryption is done, since the files are already encrypted
     * @param dto use the id in dto to get the values through the model
     */
    void export(File selectedFile, ProjectDTO dto) throws FatalException;

    /**
     * Save the canvas into a .bin file
     * Hash the file and update the database
     * Encrypt the .bin file if required in which case it deletes the non encrypted file
     * @param canvas the canvas that needs to be saved
     * @param dto the project concerned
     * @throws FatalException in case of I/O exception
     * @return the updated ProjectDTO
     */
    ProjectDTO save(Canvas canvas, ProjectDTO dto) throws FatalException;

    /**
     * Load a canvas previously saved into a .bin file
     * If the .bin.enc file has been deleted or never saved, a new Canvas is created
     * This method will also decrypt the file .bin.enc to readable .bin
     * @param dto of project concerned
     * @param password the password of the file
     * @return the Canvas
     * @throws FatalException in case of I/O exception
     */
    Canvas loadSavedCanvas(ProjectDTO dto, String password) throws FatalException;

    /**
     * Load a project previously exported in a tar.gz
     * It will ask the user the project password, decrypt the project to store the hash in the database
     * @param selectedArchive The archive to import
     * @param projectDTO      DTO with the name of the new project once imported
     * @param userDTO         Active user
     * @return the projectDTO of the newly loaded project
     * @throws FatalException in case of I/O Exception or if a project of that name already exists
     * @throws BizzException  in case of invalid or empty tar.gz
     */
    ProjectDTO load(File selectedArchive, ProjectDTO projectDTO, UserDTO userDTO) throws FatalException, BizzException;
}
