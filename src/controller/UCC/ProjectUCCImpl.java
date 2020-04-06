package controller.UCC;

import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.DTO.ProjectDTO;
import controller.ProjectImpl;
import controller.DTO.UserDTO;
import model.DALServices;
import model.DAO;
import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static utilities.Utility.checkObjects;
import static utilities.Utility.checkString;

/**
 * {@inheritDoc}
 */
public class ProjectUCCImpl implements ProjectUCC {

    //TODO WHAT USAGE ?
    private final String ContentTextImport = "impossible to import, name contains unauthorized characters... ";
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;

    private final DALServices dal;
    private final DAO<ProjectDTO> projectDAO;

    public ProjectUCCImpl(DALServices dalServices, DAO<ProjectDTO> projectDAO) {
        this.dal = dalServices;
        this.projectDAO = projectDAO;
    }

    //TODO WHY PUBLIC ?
    /**
     * {@inheritDoc}
     */
    @Override
    public void renameFolderProject(File projectName, File NewProjectName) {
        projectName.renameTo(NewProjectName);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void create(ProjectDTO dto) throws BizzException, FatalException {
        checkObjects(dto);
        String projectName = dto.getProjectName();
        checkString(projectName, "project Name");

        try {
            dal.startTransaction();
            UserDTO owner = ConnectedUser.getConnectedUser();
            String projectPath = System.getProperty("user.home") + rootProject +"userid_" +owner.getUserId() + File.separator + projectName;
            dto.setProjectOwnerId(owner.getUserId());
            dto.setCreateDate(Utility.getTimeStamp());
            dto.setModificationDate(Utility.getTimeStamp());
            dto.setProjectPath(projectPath);
            this.projectDAO.create(dto);
            //TODO move in dao
            Files.createDirectories(Paths.get(projectPath));
            ActiveProject.setActiveProject(dto);
            ActiveCanvas.setNewCanvas(-1, -1);

            dal.commit();
        } catch (IOException e) { //TODO move once file in dao
            throw new FatalException("Couldn't create project. Error failed to create a new directory.");
        } finally {
            dal.rollback();
        }

    }

}

