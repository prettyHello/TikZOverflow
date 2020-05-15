package be.ac.ulb.infof307.g09.controller.UCC;


import be.ac.ulb.infof307.g09.config.ConfigurationHolder;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveCanvas;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveProject;
import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.model.DALServices;
import be.ac.ulb.infof307.g09.model.DAO;
import be.ac.ulb.infof307.g09.model.ProjectDAO;
import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.File;
import java.util.ArrayList;

import static be.ac.ulb.infof307.g09.controller.ControllerUtility.checkObjects;
import static be.ac.ulb.infof307.g09.controller.ControllerUtility.checkString;

/**
 * {@inheritDoc}
 */
public class ProjectUCCImpl implements ProjectUCC {
    private final String rootFolder = File.separator + "ProjectTikZ" + File.separator;

    private final UserUCC userUcc = ConfigurationHolder.getUserUcc();
    private final DALServices dal;
    private final ProjectDAO projectDAO;

    public ProjectUCCImpl(DALServices dalServices, DAO<ProjectDTO> projectDAO) {
        this.dal = dalServices;
        this.projectDAO = (ProjectDAO) projectDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActive(ProjectDTO dto, String password) throws FatalException {
        checkObjects(dto);
        ActiveProject.setActiveProject(this.projectDAO.get(dto));
        Canvas canvas =  this.projectDAO.loadSavedCanvas(ActiveProject.getActiveProject(),password);
        ActiveCanvas.setActiveCanvas(canvas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(ProjectDTO dto) throws BizzException, FatalException {
        checkObjects(dto);
        String projectName = dto.getProjectName();
        checkString(projectName, "project Name");
        UserDTO owner = ConnectedUser.getConnectedUser();
        dto.setProjectOwnerId(owner.getUserId());
        dto.setCreateDate(ControllerUtility.getTimeStamp());
        dto.setModificationDate(ControllerUtility.getTimeStamp());
        String projectPath = System.getProperty("user.home") + rootFolder +"userid_" +owner.getUserId() + File.separator + projectName;
        dto.setProjectPath(projectPath);
        ProjectDTO updatedDTO = this.projectDAO.create(dto);
        ActiveProject.setActiveProject(updatedDTO);
        ActiveCanvas.setNewCanvas();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void export(File selectedFile, ProjectDTO dto) throws FatalException {
        this.projectDAO.export(selectedFile,dto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO load(File selectedFile, ProjectDTO projectDTO) throws FatalException {
        ControllerUtility.checkObjects(projectDTO);
        ControllerUtility.checkObjects(selectedFile);
        projectDTO.setCreateDate(ControllerUtility.getTimeStamp());
        projectDTO.setModificationDate(ControllerUtility.getTimeStamp());
        projectDTO.setProjectOwnerId(this.userUcc.getConnectedUser().getUserId());
        UserDTO userDTO = ConnectedUser.getConnectedUser();
        return this.projectDAO.load(selectedFile,projectDTO, userDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(ProjectDTO dto) throws FatalException {
        this.projectDAO.delete(dto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() throws FatalException{
        ProjectDTO updatedDTO = this.projectDAO.save(ActiveCanvas.getActiveCanvas(),ActiveProject.getActiveProject());
        ActiveProject.setActiveProject(updatedDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException{
        return this.projectDAO.getOwnedProjects(dto);
    }
}

