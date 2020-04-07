package model;

import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.ProjectImpl;
import controller.factories.ProjectFactory;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.File;
import java.util.ArrayList;

//TODO CHANGE APRES LE REFACTOR

public class ProjectDAOMock implements ProjectDAO {
    @Override
    public ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException {
        return null;
    }

    @Override
    public void export(File selectedFile, ProjectDTO dto) throws FatalException {

    }

    @Override
    public void save(Canvas canvas, UserDTO userDto) throws FatalException {

    }

    @Override
    public Canvas loadSavedCanvas(UserDTO userDto) throws FatalException {
        return null;
    }

    @Override
    public ProjectDTO load(File selectedFile, ProjectDTO projectDTO, UserDTO userDTO) throws FatalException {
        return null;
    }




    @Override
    public ProjectDTO get(ProjectDTO obj) throws FatalException {
        return null;
    }

    @Override
    public void create(ProjectDTO obj) throws FatalException {

    }

    @Override
    public void update(ProjectDTO obj) throws FatalException {

    }

    @Override
    public void delete(ProjectDTO obj) throws FatalException {

    }

    /*
    private ArrayList<ProjectImpl> projects;
    private DALBackEndServices dal;
    private ProjectFactory projectFactory;

    public ProjectDAOMock(DALServices dalServices, ProjectFactory projectFactory) {
        //this.dal = (DALBackEndServices) dalServices;
        this.projectFactory = projectFactory;
        projects = new ArrayList<>();

    }

    @Override
    public void create(ProjectImpl project) {
        for (ProjectImpl dto : projects) {
            if(dto.getProjectPath().equals(project.getProjectPath())){
                throw new BizzException("Project already exists");
            }
        }
        projects.add(project);
    }

    @Override
    public ArrayList<ProjectImpl> getProjects(int userID) {
        return projects;
    }

    @Override
    public ProjectImpl getSelectedProject(int userID, String projectName) throws BizzException {
        for (ProjectImpl proj : projects) {
            if(proj.getProjectOwnerId() == userID && proj.getProjectName().equals(projectName)){
                return proj;
            }
        }
        throw new BizzException("Failed to load the project: " + projectName);
    }

    @Override
    public ProjectImpl get(ProjectImpl p) {
        int project_id = p.getProjectId();
        for (ProjectImpl proj : projects) {
            if(proj.getProjectId() == project_id){
                return proj;
            }
        }
        throw new BizzException("Failed to load the project: " + project_id);
    }

    @Override
    public void delete(ProjectImpl project) {
        projects.remove(project);
    }

    @Override
    public ProjectImpl find(ProjectImpl obj) {
        return null;
    }


    @Override
    public void update(ProjectImpl obj) {

    }
    */

}
