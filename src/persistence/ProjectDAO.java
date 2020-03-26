package persistence;

import business.DTO.ProjectDTO;

import java.util.ArrayList;

public interface ProjectDAO extends DAO<ProjectDTO> {

    static ProjectDAOImpl getInstance(){
        return  new ProjectDAOImpl();
    }

    void saveNewProject(ProjectDTO project);

    ArrayList<ProjectDTO> getProjects(int userID);


    ProjectDTO getSelectedProject(int userID, String projectName) throws FatalException;

    ProjectDTO getProjectDTO(int project_id);
    void deleteProject(ProjectDTO project ) ;


}
