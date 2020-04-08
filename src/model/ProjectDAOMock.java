package model;

import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import utilities.Utility;
import utilities.exceptions.FatalException;

import java.io.File;
import java.util.ArrayList;

import static java.util.Objects.isNull;
import static utilities.Utility.checkObjects;

//TODO CHANGE APRES LE REFACTOR

public class ProjectDAOMock implements ProjectDAO {

    private ArrayList<ProjectDTO> projectList;
    private Canvas activeCanvas;
    private ProjectDTO projectDTO;
    private int projectIdCounter = 0;

    @Override
    public ArrayList<ProjectDTO> getOwnedProjects(UserDTO userDto) throws FatalException {
        checkObjects(userDto);
        if(isNull(userDto.getUserId())){
            throw new FatalException("test missing userId");
        }
        return this.projectList;
    }

    @Override
    public void export(File selectedFile, ProjectDTO dto) throws FatalException {
        checkObjects(selectedFile);
        checkObjects(dto);
        if(isNull(dto.getProjectPath())){
            throw new FatalException("test missing path");
        }
    }

    @Override
    public void save(Canvas canvas, UserDTO userDTO, ProjectDTO dto) throws FatalException {

    }

    @Override
    public Canvas loadSavedCanvas(UserDTO userDTO, ProjectDTO dto) throws FatalException {
        return null;
    }

   //@Override
    public void save(Canvas canvas, UserDTO userDto) throws FatalException {
        checkObjects(canvas);
        checkObjects(userDto);
        checkObjects(userDto.getUserId());
        if(isNull(ActiveProject.getActiveProject())){
            throw new FatalException("test no active project");
        }
        this.activeCanvas = canvas;
    }

    //@Override
    public Canvas loadSavedCanvas(UserDTO userDto) throws FatalException {
        checkObjects(userDto);
        if(isNull(ActiveProject.getActiveProject())){
            throw new FatalException("test no active project");
        }
        return this.activeCanvas;
    }

    @Override
    public ProjectDTO load(File selectedFile, ProjectDTO projectDTO, UserDTO userDTO) throws FatalException {
        checkObjects(projectDTO.getProjectName());
        checkObjects(selectedFile);
        checkObjects(userDTO);
        return null;
    }

    @Override
    public ProjectDTO get(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        checkObjects(dto.getProjectId());
        if(!this.projectList.contains(dto)){
            throw new FatalException("classic fatal");
        }
        return dto;
    }

    @Override
    public void create(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        checkObjects(dto.getProjectOwnerId());
        checkObjects(dto.getProjectName());
        checkObjects(dto.getProjectPath());
        checkObjects(dto.getCreateDate());
        checkObjects(dto.getModificationDate());
        dto.setProjectId(projectIdCounter++);
        this.projectList.add(dto);
        ActiveCanvas.setNewCanvas(-1, -1);
        this.activeCanvas = ActiveCanvas.getActiveCanvas();
        ActiveProject.setActiveProject(dto);
        this.projectDTO = ActiveProject.getActiveProject();
    }

    @Override
    public void update(ProjectDTO obj) throws FatalException {
        //Empty for now
    }

    @Override
    public void delete(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        checkObjects(dto.getProjectId());
        this.projectList.remove(dto);
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
