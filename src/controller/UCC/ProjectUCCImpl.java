package controller.UCC;

import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.factories.ProjectFactoryImpl;
import model.DALServices;
import model.DAO;
import model.ProjectDAO;
import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * {@inheritDoc}
 */
public class ProjectUCCImpl implements ProjectUCC {

    private final String ContentTextImport = "impossible to import, name contains unauthorized characters... ";
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;

    private final DALServices dal;
    private final DAO<ProjectDTO> projectDAO;

    public ProjectUCCImpl(DALServices dalServices, DAO<ProjectDTO> projectDAO) {
        this.dal = dalServices;
        this.projectDAO = projectDAO;
    }

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
    public ProjectDTO getProjectDTO(String projectName, Path folderDestination, int userId) {
        return new ProjectDTO().
                setProjectOwnerId(userId)
                .setProjectName(projectName)
                .setProjectPath(folderDestination.toString()+ File.separator +projectName)
                .setCreateDate(Utility.getTimeStamp())
                .setModificationDate(Utility.getTimeStamp());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO get(ProjectDTO project) {
        return ((ProjectDAO) projectDAO).get(project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewProject(String projectName) throws BizzException, FatalException {
        if(projectName == null || projectName.isEmpty()){
            throw new IllegalArgumentException("project name can't be null nor empty");
        }

        try {
            dal.startTransaction();
            try {
                // database
                UserDTO owner = ConnectedUser.getConnectedUser();
                String now = Utility.getTimeStamp();
                String projectPath = System.getProperty("user.home") + rootProject +"userid_" +owner.getUserId() + File.separator + projectName;
                ProjectDTO projectDTO = new ProjectFactoryImpl().createProject(0, owner.getUserId(), projectName, "", projectPath, now, now);
                ((ProjectDAO) projectDAO).create(projectDTO);

                // filesystem
                Files.createDirectories(Paths.get(projectPath));

                ActiveProject.setActiveProject(projectDTO);
                ActiveCanvas.setNewEmptyCanvas(-1, -1);
            } catch (BizzException e) {
                throw new BizzException("Couldn't create project. Project name already in use");
            } catch (IOException e) {
                throw new BizzException("Couldn't create project. Error during project folder creation");
            }
            dal.commit();
        } finally {
            dal.rollback();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createFromImport(ProjectDTO projectDTO) throws BizzException, IOException {
         projectDAO.create(projectDTO);
    }
}

