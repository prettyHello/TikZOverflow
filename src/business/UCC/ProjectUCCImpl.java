package business.UCC;

import business.Canvas.ActiveCanvas;
import business.Canvas.ActiveProject;
import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import business.factories.ProjectFactoryImpl;
import exceptions.BizzException;
import exceptions.FatalException;
import persistence.DALServices;
import persistence.DAO;
import persistence.ProjectDAO;
import utilities.Utility;

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
    public ProjectDTO getProjectDTO(int project_id) {
        return ((ProjectDAO) projectDAO).getProjectDTO(project_id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewProject(String projectName) throws BizzException, FatalException {
        try {
            dal.startTransaction();
            try {
                // database
                UserDTO owner = ConnectedUser.getConnectedUser();
                String now = Utility.getTimeStamp();
                String projectPath = System.getProperty("user.home") + rootProject +"userid_" +owner.getUserId() + File.separator + projectName;
                ProjectDTO projectDTO = new ProjectFactoryImpl().createProject(0, owner.getUserId(), projectName, "", projectPath, now, now);
                ((ProjectDAO) projectDAO).saveNewProject(projectDTO);

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
        ((ProjectDAO) projectDAO).saveNewProject(projectDTO);
    }
}

