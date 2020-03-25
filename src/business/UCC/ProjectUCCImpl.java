package business.UCC;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import business.factories.ProjectFactoryImpl;
import exceptions.BizzException;
import persistence.DALServices;
import persistence.DAO;
import persistence.ProjectDAO;
import utilities.Utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ProjectUCCImpl implements ProjectUCC {

    private final String ContentTextImport = "impossible to import, name contains unauthorized characters... ";

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
                .setProjectPath(folderDestination.toString())
                .setCreateDate(Utility.getTimeStamp())
                .setModificationDate(Utility.getTimeStamp());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewProject(String projectName) throws BizzException {
        try {
            dal.startTransaction();
            try {
                // database
                UserDTO owner = ConnectedUser.getConnectedUser();
                String now = Utility.getTimeStamp();
                String projectPath = "resourcesprojects/userid" + owner.getUser_id() + "/" + projectName;
                ProjectDTO projectDTO = new ProjectFactoryImpl().createProject(0, owner.getUser_id(), projectName, "", projectPath, now, now);
                ((ProjectDAO) projectDAO).saveNewProject(projectDTO);

                // filesystem
                Files.createDirectories(Paths.get("resources/projects/userid_" + ConnectedUser.getConnectedUser().getUser_id() + "/" + projectName));
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

    @Override
    public void createFromImport(String importPath) throws BizzException, IOException {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
