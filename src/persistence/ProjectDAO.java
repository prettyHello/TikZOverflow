package persistence;

import business.DTO.ProjectDTO;
import utilities.Utility;

import javax.rmi.CORBA.Util;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


public class ProjectDAO {
    PreparedStatement prstmt;
    private static final String SQL_INSERT_PROJECT = "INSERT INTO projects(project_id, project_owner_id, name, path, creation_date, modification_date ) VALUES (?, ?, ?, ?, ?, ?)";


    DALServicesImpl querry = new DALServicesImpl() ;

   private ProjectDAO(){}

    public static ProjectDAO getInstance() {
       return  new ProjectDAO();
    }

    public void saveProject(ProjectDTO project){
        try {
           prstmt = querry.prepareStatement(SQL_INSERT_PROJECT);
            prstmt.setInt(1, 29);
           prstmt.setInt(2, 37);
           prstmt.setString(3, project.getProjectName());
           prstmt.setString(4, project.getProjectPath());
           prstmt.setString(5, Utility.getTimeStamp());
           prstmt.setString(6, Utility.getTimeStamp());
           prstmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Error dans la BD. UNIQUE constraint failed: projects.project_id doit etre unique ");
        }
    }
}
