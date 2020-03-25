package persistence;

import business.DTO.ProjectDTO;
import exceptions.FatalException;

import java.util.ArrayList;

public interface ProjectDAO {
    static ProjectDAOImpl getInstance(){
        return  new ProjectDAOImpl();
    }

    void saveProject(ProjectDTO project) throws FatalException;

    ArrayList<ProjectDTO> getProjects(int userID) throws FatalException;

    ProjectDTO getSelectedProject(int userID, String projectName) throws FatalException;
}
