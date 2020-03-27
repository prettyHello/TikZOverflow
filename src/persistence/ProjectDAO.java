package persistence;

import business.DTO.ProjectDTO;
import exceptions.FatalException;

import java.util.ArrayList;


public interface ProjectDAO extends DAO<ProjectDTO> {


    void saveNewProject(ProjectDTO project);

    ArrayList<ProjectDTO> getProjects(int userID);


    ProjectDTO getSelectedProject(int userID, String projectName) throws FatalException;

    ProjectDTO getProjectDTO(int project_id);
    void deleteProject(ProjectDTO project ) ;


}
