package persistence;

import business.DTO.ProjectDTO;
import business.UCC.ProjectUCC;
import business.factories.ProjectFactory;
import exceptions.BizzException;
import exceptions.FatalException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class ProjectDAOMock implements ProjectDAO {

    private ArrayList<ProjectDTO> projects;
    private DALBackEndServices dal;
    private ProjectFactory projectFactory;

    public ProjectDAOMock(DALServices dalServices, ProjectFactory projectFactory) {
        //this.dal = (DALBackEndServices) dalServices;
        this.projectFactory = projectFactory;
        projects = new ArrayList<>();

    }

    @Override
    public void saveNewProject(ProjectDTO project) {
        for (ProjectDTO dto : projects) {
            if(dto.getProjectPath().equals(project.getProjectPath())){
                throw new BizzException("Project already exists");
            }
        }
        projects.add(project);
    }

    @Override
    public ArrayList<ProjectDTO> getProjects(int userID) {
        return projects;
    }

    @Override
    public ProjectDTO getSelectedProject(int userID, String projectName) throws BizzException {
        for (ProjectDTO proj : projects) {
            if(proj.getProjectOwnerId() == userID && proj.getProjectName().equals(projectName)){
                return proj;
            }
        }
        throw new BizzException("Failed to load the project: " + projectName);

    }

    @Override
    public ProjectDTO getProjectDTO(int project_id) {
        for (ProjectDTO proj : projects) {
            if(proj.getProjectId() == project_id){
                return proj;
            }
        }
        throw new BizzException("Failed to load the project: " + project_id);
    }

    @Override
    public void deleteProject(ProjectDTO project) {
        projects.remove(project);
    }

    @Override
    public ProjectDTO find(ProjectDTO obj) {
        return null;
    }

    @Override
    public void create(ProjectDTO obj) throws FatalException {

    }

    @Override
    public void update(ProjectDTO obj) {

    }

    @Override
    public void delete(ProjectDTO obj) {

    }

    @Override
    public ProjectDTO getUser(ProjectDTO user) {
        return null;
    }
}
