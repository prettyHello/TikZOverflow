package model;

import controller.DTO.ProjectDTO;
import controller.factories.ProjectFactory;
import utilities.exceptions.BizzException;

import java.util.ArrayList;

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
    public void create(ProjectDTO project) {
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
    public ProjectDTO get(ProjectDTO p) {
        int project_id = p.getProjectId();
        for (ProjectDTO proj : projects) {
            if(proj.getProjectId() == project_id){
                return proj;
            }
        }
        throw new BizzException("Failed to load the project: " + project_id);
    }

    @Override
    public void delete(ProjectDTO project) {
        projects.remove(project);
    }

    @Override
    public ProjectDTO find(ProjectDTO obj) {
        return null;
    }


    @Override
    public void update(ProjectDTO obj) {

    }




}
