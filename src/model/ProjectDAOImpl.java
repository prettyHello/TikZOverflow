package model;

import controller.DTO.ProjectDTO;
import controller.factories.ProjectFactory;
import javafx.scene.control.Alert;
import utilities.exceptions.BizzException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// creer une interface pour projetDAO, ensuite modifier la classe ProjectDAO en ProjectDAOImpl

//TODO CLASSE : variable camecase, Implementé dao correctement.
//

/**
 * {@inheritDoc}
 */
public class ProjectDAOImpl implements ProjectDAO {

    //TODO PREPAREDSTAT dois venir du dal je,crois
    PreparedStatement prstmt;
    private static final String SQL_INSERT_PROJECT = "INSERT INTO projects(project_owner_id, name, path, creation_date, modification_date ) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_PROJECT = "SELECT * FROM projects WHERE project_owner_id = ?";
    private static final String SQL_SELECT_BY_PROJECTID = "SELECT * FROM projects WHERE project_id = ?";
    private static final String SQL_SELECT_PROJECT_OF_USER = "SELECT * FROM projects WHERE project_owner_id = ?  AND name = ?";
    private static final String SQL_DELETE_PROJECT_OF_USER = "DELETE FROM projects WHERE project_owner_id = ? AND name = ?";
    //TODO CONMPRENDRE CE QUE 9A VOUSLAIS DIRE : gerer les connections en s'appuyant sur l'implementation dans UserUCCImpl pour les fermetures et Exceptions

    private final DALBackEndServices dal;
    private final ProjectFactory projectFactory;

    //TODO check si on utilise le bon dal.
    public ProjectDAOImpl(DALServices dalServices, ProjectFactory projectFactory) {
        this.dal = (DALBackEndServices) dalServices;
        this.projectFactory = projectFactory;
    }



    /**
     * {@inheritDoc}
     */
    public void create(ProjectDTO project) {
        try {
            prstmt = dal.prepareStatement(SQL_INSERT_PROJECT);
            prstmt.setInt(1, project.getProjectOwnerId());
            prstmt.setString(2, project.getProjectName());
            prstmt.setString(3, project.getProjectPath());
            prstmt.setString(4, project.getCreateDate());
            prstmt.setString(5, project.getModificationDate());
            prstmt.executeUpdate();
        } catch (SQLException e) {
            //TODO : lever des utilities.exceptions de type FATAL...
            throw new BizzException("Project already exists");
        }
    }

    /**
     * {@inheritDoc}
     */
    //TODO change en projetdto
    @Override
    public ArrayList<ProjectDTO> getProjects(int userID) {
        PreparedStatement pr;
        ResultSet rs;
        ArrayList<ProjectDTO> projects = new ArrayList<>();
        try {
            pr = this.dal.prepareStatement(SQL_SELECT_PROJECT);
            pr.setInt(1, userID);
            rs = pr.executeQuery();
            while (rs.next()) {
                ProjectDTO project = fillDTO(rs);
                projects.add(project);
            }
            return projects;
            //TODO CATCH PLUS PRECIS
        } catch (Exception e) {
            //TODO FATAL
            throw new BizzException("Failed to load project list");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO get(ProjectDTO p) {
        int project_id = p.getProjectId();
        PreparedStatement pr;
        ResultSet rs;
        try {
            pr = this.dal.prepareStatement(SQL_SELECT_BY_PROJECTID);
            pr.setInt(1, project_id);
            rs = pr.executeQuery();
            //TODO le resultSet est potentielement vide, donc fermé, ce qui crée un bug.
            rs.next();
            return fillDTO(rs);
        } catch (Exception e) {
            //TODO PUE
            e.printStackTrace();
        }
        //TODO PAS RETURN NULL
        return null;
    }

    /**
     * {@inheritDoc}
     */
    //TODO CHECK SI PAS UN DOUBLE DE METHODE AU DESSU
    @Override
    public ProjectDTO getSelectedProject(int userID, String projectName) throws BizzException {
        PreparedStatement pr;
        ResultSet rs;
        ProjectDTO project = new ProjectDTO();
        try {
            pr = this.dal.prepareStatement(SQL_SELECT_PROJECT_OF_USER);
            pr.setInt(1, userID);
            pr.setString(2, projectName);
            rs = pr.executeQuery();
            while (rs.next()) {
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectOwnerId(rs.getInt("project_owner_id"));
                project.setProjectName(rs.getString("name"));
                project.setCreateDate(rs.getString("creation_date"));
                project.setModificationDate(rs.getString("modification_date"));
                project.setProjectPath(rs.getString("path"));
            }
            return project;
        } catch (Exception e) {
            throw new BizzException("Failed to load the project: " + project.getProjectName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(ProjectDTO project) {
        PreparedStatement pr;
        try {
            pr = this.dal.prepareStatement(SQL_DELETE_PROJECT_OF_USER);
            pr.setInt(1, project.getProjectOwnerId());
            pr.setString(2, project.getProjectName());
            pr.executeUpdate();
        } catch (Exception e) {
            //TODO this is not MVC, should throw a bizzException and not use javafx in the model
            new Alert(Alert.AlertType.ERROR, "Failed to Delete the project '" + project.getProjectName() + "' in Database").showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO find(ProjectDTO obj) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ProjectDTO obj) {

    }

    private ProjectDTO fillDTO(ResultSet rs) throws SQLException {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(rs.getInt("project_id"));
        projectDTO.setProjectOwnerId(rs.getInt("project_owner_id"));
        projectDTO.setProjectName(rs.getString("name"));
        projectDTO.setProjectPath(rs.getString("path"));
        projectDTO.setCreateDate(rs.getString("creation_date"));
        projectDTO.setModificationDate(rs.getString("modification_date"));
        return projectDTO;
    }
}
