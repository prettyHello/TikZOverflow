package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

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
     * @param dto use the id in dto to get the values through the be.ac.ulb.infof307.g09.model
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

    /**
     * Tell to ActiveProject which project is active and send to ActiveCanvas the corresponding canvas
     * @param dto project to be marked as active
     * @throws FatalException Transmit the eventual FatalException sent by the Model to the be.ac.ulb.infof307.g09.view
     */
    void setActive(ProjectDTO dto) throws FatalException;

    /**
     * Return the list of projects owned by a user
     * @param dto the User whose project list we wants
     * @return the list of projects owned by that user
     * @throws FatalException Transmit the eventual FatalException sent by the Model to the be.ac.ulb.infof307.g09.view
     */
    ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException;
}
