package persistence;

import business.DTO.ProjectDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ProjectDAO {
    PreparedStatement prstmt;
    private static final String SQL_INSERT_PROJECT = "INSERT INTO projects(name, path, creation_date, modification_date ) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_PROJECT = "SELECT * FROM projects";

    DALServicesImpl querry = new DALServicesImpl() ;

    private ProjectDAO(){}
    public static ProjectDAO getInstance() {
       return  new ProjectDAO();
    }

    public void saveProject(ProjectDTO project){
        try {
           prstmt = querry.prepareStatement(SQL_INSERT_PROJECT);
           //prstmt.setInt(1, 29);
           //prstmt.setInt(2, 37);
           prstmt.setString(1, project.getProjectName().get(0));
           prstmt.setString(2, project.getProjectPath().get(0));
           prstmt.setString(3, project.getCreateDate().get(0));
           prstmt.setString(4, project.getModificationDate().get(0));
           prstmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Error dans la BD. UNIQUE constraint failed: projects.project_id doit etre unique ");
        }
    }

    public ProjectDTO getProject(){
        PreparedStatement pr;
        ResultSet rs;
        ProjectDTO project =new ProjectDTO();
        try {
            pr = ((DALBackEndServices) this.querry).prepareStatement(SQL_SELECT_PROJECT);
            rs = pr.executeQuery();
            while (rs.next()) {
                project.setProjectName(rs.getString("name"));
                project.setCreateDate(rs.getString("creation_date"));
                project.setModificationDate(rs.getString("modification_date"));
                project.setProjectPath(rs.getString("path"));
            }
            return project;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
