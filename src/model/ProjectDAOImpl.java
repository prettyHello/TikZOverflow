package model;

import controller.DTO.ProjectDTO;
import controller.ProjectImpl;
import controller.DTO.UserDTO;
import controller.factories.ProjectFactory;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static utilities.Utility.checkObjects;

/**
 * {@inheritDoc}
 */
public class ProjectDAOImpl implements ProjectDAO {


    private static final String SQL_INSERT_PROJECT = "INSERT INTO projects(project_owner_id, name, path, creation_date, modification_date ) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_PROJECT = "SELECT * FROM projects WHERE project_owner_id = ?";
    private static final String SQL_SELECT_BY_PROJECTID = "SELECT * FROM projects WHERE project_id = ?";
    private static final String SQL_DELETE_PROJECT_OF_USER = "DELETE FROM projects WHERE project_owner_id = ? AND name = ?";

    private final DALBackEndServices dal;
    private final ProjectFactory projectFactory;


    public ProjectDAOImpl(DALServices dalServices, ProjectFactory projectFactory) {
        this.dal = (DALBackEndServices) dalServices;
        this.projectFactory = projectFactory;
    }



    /**
     * {@inheritDoc}
     */
    public void create(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        PreparedStatement pr;
        try {
            pr = dal.prepareStatement(SQL_INSERT_PROJECT);
            pr.setInt(1, dto.getProjectOwnerId());
            pr.setString(2, dto.getProjectName());
            pr.setString(3, dto.getProjectPath());
            pr.setString(4, dto.getCreateDate());
            pr.setString(5, dto.getModificationDate());
            pr.executeUpdate();
        } catch (SQLException e) {
            throw new FatalException("Project already exists");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ProjectImpl> getOwnedProjects(UserDTO dto) throws FatalException {
        checkObjects(dto);
        PreparedStatement pr;
        ResultSet rs;
        ArrayList<ProjectImpl> projects = new ArrayList<>();

        try {
            pr = this.dal.prepareStatement(SQL_SELECT_PROJECT);
            pr.setInt(1, dto.getUserId());
            rs = pr.executeQuery();
            while (rs.next()) {
                ProjectImpl project = fillDTO(rs);
                projects.add(project);
            }
        } catch (SQLException e) {
            throw new FatalException("SQLException in projectDAOImpl: impossible to load the project list");
        }
        return projects;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectImpl get(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        PreparedStatement pr;
        ResultSet rs;
        ProjectImpl result;

        try {
            pr = this.dal.prepareStatement(SQL_SELECT_BY_PROJECTID);
            pr.setInt(1, dto.getProjectId());
            rs = pr.executeQuery();
            if(rs.next()){
                result = fillDTO(rs);
            } else {
                throw new BizzException("Project with id : " + dto.getProjectId() + " does not exist");
            }
        } catch (SQLException e) {
            throw new FatalException("SQLException in projectDAOImpl:"+e.getMessage());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        PreparedStatement pr;
        try {
            pr = this.dal.prepareStatement(SQL_DELETE_PROJECT_OF_USER);
            pr.setInt(1, dto.getProjectOwnerId());
            pr.setString(2, dto.getProjectName());
            pr.executeUpdate();
        } catch (SQLException e) {
            throw new FatalException("Failed to Delete the project '" + dto.getProjectName() + "' in Database");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ProjectDTO obj) {
        //TODO
    }

    /**
     * Fill a new DTO with the data from the result set
     * @param rs result set
     * @return The newly created DTO
     * @throws SQLException
     */
    private ProjectImpl fillDTO(ResultSet rs) throws SQLException {
        ProjectImpl projectDTO = projectFactory.createProject();
        projectDTO.setProjectId(rs.getInt("project_id"));
        projectDTO.setProjectOwnerId(rs.getInt("project_owner_id"));
        projectDTO.setProjectName(rs.getString("name"));
        projectDTO.setProjectPath(rs.getString("path"));
        projectDTO.setCreateDate(rs.getString("creation_date"));
        projectDTO.setModificationDate(rs.getString("modification_date"));
        return projectDTO;
    }
}
