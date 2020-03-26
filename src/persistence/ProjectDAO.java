package persistence;

import business.DTO.ProjectDTO;
import exceptions.FatalException;

import java.util.ArrayList;

public interface ProjectDAO {
    static ProjectDAOImpl getInstance(){
        return  new ProjectDAOImpl();
    }

    /**
     * Save a project into a DataBase
     * @param project Project object to be saved
     */
    void saveProject(ProjectDTO project) throws FatalException;

    /**
     * Retrieve the project list of connected user
     * @param userID User ID
     */
    ArrayList<ProjectDTO> getProjects(int userID) throws FatalException;

    /**
     * Retrieve a specific project of connected user
     * @param userID User ID
     * @param projectName Project name
     */
    ProjectDTO getSelectedProject(int userID, String projectName) throws FatalException;

    void deleteProject(ProjectDTO project ) ;
}
