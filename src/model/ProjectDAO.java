package model;

import controller.DTO.ProjectDTO;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.util.ArrayList;

/**
 * Interface for database action regarding projects
 */
public interface ProjectDAO extends DAO<ProjectDTO> {

    /**
     * Retrieve a project from the database
     *
     * @param userID the id of the owner of the project to retrieve
     * @return the project DTO
     */
    ArrayList<ProjectDTO> getProjects(int userID);

    /**
     * @param userID
     * @param projectName
     * @return
     * @throws FatalException
     */
    //TODO CHECK is if bizzexception is the right one
    ProjectDTO getSelectedProject(ProjectDTO dto) throws BizzException;
}
