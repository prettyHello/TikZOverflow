package controller.UCC;

import config.ConfigurationSingleton;
import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import model.DALServices;
import model.DAO;
import model.ProjectDAO;
import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.File;
import java.util.ArrayList;

import static utilities.Utility.checkObjects;
import static utilities.Utility.checkString;

/**
 * {@inheritDoc}
 */
public class ProjectUCCImpl implements ProjectUCC {
    private String rootFolder = File.separator + "ProjectTikZ" + File.separator;

    private UserUCC userUcc = ConfigurationSingleton.getUserUcc();
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
        ActiveProject.setActiveProject(this.projectDAO.get(dto));
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
    public Canvas loadSavedCanvas()throws FatalException {
        return this.projectDAO.loadSavedCanvas( this.userUcc.getConnectedUser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() throws FatalException{
        this.projectDAO.save(ActiveCanvas.getActiveCanvas(),this.userUcc.getConnectedUser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException{
        return this.projectDAO.getOwnedProjects(dto);
    }
}

