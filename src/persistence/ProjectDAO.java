package persistence;

import business.DTO.ProjectDTO;

import java.util.ArrayList;

public interface ProjectDAO {
    static ProjectDAOImpl getInstance() {
        return  new ProjectDAOImpl();
    }

    void saveProject(ProjectDTO project);

    ArrayList<ProjectDTO> getProjects(int userID);

    ProjectDTO getSelectedProject(int userID, String projectName);
}
