package persistence;

import business.DTO.ProjectDTO;
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
    private static final String SQL_SELECT_PROJECT_OF_USER = "SELECT * FROM projects WHERE project_owner_id = ?  AND name = ?";
    private static final String SQL_DELETE_PROJECT_OF_USER = "DELETE FROM projects WHERE project_owner_id = ? AND name = ?";
    // gerer les connections en s'appuyant sur l'implementation dans UserUCCImpl pour les fermetures et Exceptions
    DALServicesImpl querry = new DALServicesImpl() ;

    ProjectDAOImpl(){}

    // lever des exceptions de type FATAL...

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProject(ProjectDTO project){
        try {
           prstmt = querry.prepareStatement(SQL_INSERT_PROJECT);
           prstmt.setInt(1, project.getProjectOwnerId());
           prstmt.setString(2, project.getProjectName());
           prstmt.setString(3, project.getProjectPath());
           prstmt.setString(4, project.getCreateDate());
           prstmt.setString(5, project.getModificationDate());
           prstmt.executeUpdate();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save '"+project.getProjectName() +"'  project").showAndWait();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ProjectDTO> getProjects(int userID){
        PreparedStatement pr;
        ResultSet rs;
        ArrayList<ProjectDTO> projects =new ArrayList<>();
        try {
            pr = ((DALBackEndServices) this.querry).prepareStatement(SQL_SELECT_PROJECT);
            pr.setInt(1, userID);
            rs = pr.executeQuery();
            while (rs.next()) {
                ProjectDTO project =  new ProjectDTO();
                project.setProjectName(rs.getString("name"));
                project.setCreateDate(rs.getString("creation_date"));
                project.setModificationDate(rs.getString("modification_date"));
                project.setProjectPath(rs.getString("path"));
                projects.add(project);
            }
            return projects;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load project list").showAndWait();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO getSelectedProject(int userID, String projectName){
        PreparedStatement pr;
        ResultSet rs;
        ProjectDTO project =  new ProjectDTO();
        try {
            pr = ((DALBackEndServices) this.querry).prepareStatement(SQL_SELECT_PROJECT_OF_USER);
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
            new Alert(Alert.AlertType.ERROR, "Failed to load the project: "+ project.getProjectName() ).showAndWait();
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

}
