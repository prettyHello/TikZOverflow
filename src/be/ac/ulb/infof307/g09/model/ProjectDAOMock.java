package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.controller.Canvas.ActiveCanvas;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveProject;
import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.File;
import java.util.ArrayList;

import static java.util.Objects.isNull;
import static be.ac.ulb.infof307.g09.controller.Utility.checkObjects;

/**
 * Implementation used for the tests of the controllers
 * Using a mock allow to test the controllers independently from the model implementation
 */
public class ProjectDAOMock implements ProjectDAO {

    public ProjectDAOMock(DALServices dalServices, ProjectFactory projectFactory) {
    }

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
    public void save(Canvas canvas,  ProjectDTO projectDTO) throws FatalException {
        checkObjects(canvas);
        checkObjects(projectDTO);
        checkObjects(projectDTO.getProjectName());
        if(isNull(ActiveProject.getActiveProject())){
            throw new FatalException("test no active project");
        }
        this.activeCanvas = canvas;
    }

    @Override
    public Canvas loadSavedCanvas(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
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
        return projectDTO;
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
        ActiveCanvas.setNewCanvas();
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

}
