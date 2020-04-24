package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.config.ConfigurationSingleton;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveCanvas;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveProject;
import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.model.DALServices;
import be.ac.ulb.infof307.g09.model.DAO;
import be.ac.ulb.infof307.g09.model.ProjectDAO;
import be.ac.ulb.infof307.g09.utilities.Utility;
import be.ac.ulb.infof307.g09.utilities.exceptions.BizzException;
import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.util.ArrayList;

import static be.ac.ulb.infof307.g09.utilities.Utility.checkObjects;
import static be.ac.ulb.infof307.g09.utilities.Utility.checkString;

/**
 * {@inheritDoc}
 */
public class ProjectUCCImpl implements ProjectUCC {
    private final String rootFolder = File.separator + "ProjectTikZ" + File.separator;

    private final UserUCC userUcc = ConfigurationSingleton.getUserUcc();
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
    public void setActive(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        ActiveProject.setActiveProject(this.projectDAO.get(dto));
        Canvas canvas =  this.projectDAO.loadSavedCanvas(ActiveProject.getActiveProject());
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
        dto.setCreateDate(Utility.getTimeStamp());
        dto.setModificationDate(Utility.getTimeStamp());
        String projectPath = System.getProperty("user.home") + rootFolder +"userid_" +owner.getUserId() + File.separator + projectName;
        dto.setProjectPath(projectPath);
        this.projectDAO.create(dto);

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
        Utility.checkObjects(projectDTO);
        Utility.checkObjects(selectedFile);
        projectDTO.setCreateDate(Utility.getTimeStamp());
        projectDTO.setModificationDate(Utility.getTimeStamp());
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
        this.projectDAO.save(ActiveCanvas.getActiveCanvas(),ActiveProject.getActiveProject());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException{
        return this.projectDAO.getOwnedProjects(dto);
    }
}

