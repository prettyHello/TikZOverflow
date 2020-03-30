package persistence;

import business.DTO.ProjectDTO;
import exceptions.BizzException;
import exceptions.FatalException;

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
    ProjectDTO getSelectedProject(int userID, String projectName) throws BizzException;




}
