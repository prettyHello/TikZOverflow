package model;

import controller.DTO.ProjectDTO;
import controller.ProjectImpl;
import controller.DTO.UserDTO;
import utilities.exceptions.FatalException;

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
    ArrayList<ProjectImpl> getOwnedProjects(UserDTO dto) throws FatalException;


}
