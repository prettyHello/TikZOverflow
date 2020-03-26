package persistence;

import business.DTO.ProjectDTO;
import business.factories.ProjectFactory;
import exceptions.BizzException;
import exceptions.FatalException;

import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// creer une interface pour projetDAO, ensuite modifier la classe ProjectDAO en ProjectDAOImpl

public class ProjectDAOImpl implements ProjectDAO {
    PreparedStatement prstmt;
    private static final String SQL_INSERT_PROJECT = "INSERT INTO projects(project_owner_id, name, path, creation_date, modification_date ) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_PROJECT = "SELECT * FROM projects WHERE project_owner_id = ?";
    private static final String SQL_SELECT_BY_PROJECTID = "SELECT * FROM projects WHERE project_id = ?";
    private static final String SQL_SELECT_PROJECT_OF_USER = "SELECT * FROM projects WHERE project_owner_id = ?  AND name = ?";
    private static final String SQL_DELETE_PROJECT_OF_USER = "DELETE FROM projects WHERE project_owner_id = ? AND name = ?";
    // gerer les connections en s'appuyant sur l'implementation dans UserUCCImpl pour les fermetures et Exceptions

    private final DALBackEndServices dal;
    private final ProjectFactory projectFactory;

    public ProjectDAOImpl(DALServices dalServices, ProjectFactory projectFactory) {
        this.dal = (DALBackEndServices) dalServices;
        this.projectFactory = projectFactory;
    }

    // lever des exceptions de type FATAL...

    @Override
    public void saveNewProject(ProjectDTO project) {
        try {
            prstmt = dal.prepareStatement(SQL_INSERT_PROJECT);
            prstmt.setInt(1, project.getProjectOwnerId());
            prstmt.setString(2, project.getProjectName());
            prstmt.setString(3, project.getProjectPath());
            prstmt.setString(4, project.getCreateDate());
            prstmt.setString(5, project.getModificationDate());
            prstmt.executeUpdate();
        } catch (SQLException e) {
            throw new BizzException("Project already exists");
        }
    }


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
        } catch (Exception e) {
            throw new BizzException("Failed to load project list");
        }
        return null;
    }

    @Override
    public ProjectDTO getProjectDTO(int project_id) {
        PreparedStatement pr;
        ResultSet rs;
        try {
            pr = this.dal.prepareStatement(SQL_SELECT_BY_PROJECTID);
            pr.setInt(1, project_id);
            rs = pr.executeQuery();
            rs.next();
            return fillDTO(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ProjectDTO getSelectedProject(int userID, String projectName) {
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
            throw new BizzException("Failed to load the project: "+ project.getProjectName());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProject(ProjectDTO project ){
        PreparedStatement pr;

        try {
            pr = ((DALBackEndServices) this.querry).prepareStatement(SQL_DELETE_PROJECT_OF_USER);
            pr.setInt(1, project.getProjectOwnerId());
            pr.setString(2, project.getProjectName());
            pr.executeUpdate();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to Delete the project '"+ project.getProjectName() + "' in Database" ).showAndWait();
            e.printStackTrace();
        }
    }

    @Override
    public ProjectDTO find(ProjectDTO obj) {
        return null;
    }

    @Override
    public void create(ProjectDTO obj) throws FatalException {
    }

    @Override
    public void update(ProjectDTO obj) {

    }

    @Override
    public void delete(ProjectDTO obj) {

    }

    @Override
    public ProjectDTO getUser(ProjectDTO user) {
        return null;
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
