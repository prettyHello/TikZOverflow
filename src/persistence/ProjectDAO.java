package persistence;

import business.DTO.ProjectDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ProjectDAO {
    PreparedStatement prstmt;
    private static final String SQL_INSERT_PROJECT = "INSERT INTO projects(project_owner_id, name, path, creation_date, modification_date ) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_PROJECT = "SELECT * FROM projects WHERE project_owner_id = ?";
    private static final String SQL_SELECT_PROJECT_OF_USER = "SELECT * FROM projects WHERE project_owner_id = ?  AND name = ?";

    DALServicesImpl querry = new DALServicesImpl() ;

    private ProjectDAO(){}
    public static ProjectDAO getInstance() {
       return  new ProjectDAO();
    }

    public void saveProject(ProjectDTO project){
        try {
           prstmt = querry.prepareStatement(SQL_INSERT_PROJECT);
           //prstmt.setInt(1, 29);
           prstmt.setInt(1, project.getProjectOwnerId());
           prstmt.setString(2, project.getProjectName());
           prstmt.setString(3, project.getProjectPath());
           prstmt.setString(4, project.getCreateDate());
           prstmt.setString(5, project.getModificationDate());
           prstmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Error dans la BD. UNIQUE constraint failed: projects.project_id doit etre unique ");
        }
    }

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
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public ArrayList<ProjectDTO> getSelectedProject(int userID, String projectName){
        PreparedStatement pr;
        ResultSet rs;
        ArrayList<ProjectDTO> projects =new ArrayList<>();
        try {
            pr = ((DALBackEndServices) this.querry).prepareStatement(SQL_SELECT_PROJECT_OF_USER);
            pr.setInt(1, userID);
            pr.setString(2, projectName);
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
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
