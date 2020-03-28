package persistence;

import business.DTO.ProjectDTO;
import exceptions.FatalException;

import java.util.ArrayList;

/**
 * Interface for database action regarding projects
 */
public interface ProjectDAO extends DAO<ProjectDTO> {

    /**
     * Adds a new project to the database
     *
     * @param project the project to save
     */
    void saveNewProject(ProjectDTO project);

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
    ProjectDTO getSelectedProject(int userID, String projectName) throws FatalException;

    /**
     * Retrieve a project from the database
     *
     * @param project_id the id of the project to retrieve
     * @return the project DTO
     */
    ProjectDTO getProjectDTO(int project_id);

    /**
     * Deletes a project from the database
     *
     * @param project the project to delete
     */
    void deleteProject(ProjectDTO project);
}
