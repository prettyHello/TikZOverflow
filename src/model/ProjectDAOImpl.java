package model;

import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.ProjectImpl;
import controller.factories.ProjectFactory;
import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;

import static utilities.Utility.*;


/**
 * {@inheritDoc}
 */
public class ProjectDAOImpl implements ProjectDAO {
    private String rootFolder = File.separator + "ProjectTikZ" + File.separator;

    private static final String SQL_INSERT_PROJECT = "INSERT INTO projects(project_owner_id, name, path, creation_date, modification_date ) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_PROJECT = "SELECT * FROM projects WHERE project_owner_id = ?";
    private static final String SQL_SELECT_BY_PROJECTID = "SELECT * FROM projects WHERE project_id = ?";
    private static final String SQL_DELETE_PROJECT_OF_USER = "DELETE FROM projects WHERE project_id = ?";

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
            Files.createDirectories(Paths.get(dto.getProjectPath()));
            ActiveProject.setActiveProject(dto);
            //TODO, Why ?
            ActiveCanvas.setNewCanvas(-1, -1);
        } catch (SQLException e) {
            throw new FatalException("Project already exists");
        } catch (IOException e){
            throw new FatalException("Couldn't create folder");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException {
        checkObjects(dto);
        PreparedStatement pr;
        ResultSet rs;
        ArrayList<ProjectDTO> projects = new ArrayList<>();
        try {
            pr = this.dal.prepareStatement(SQL_SELECT_PROJECT);
            pr.setInt(1, dto.getUserId());
            rs = pr.executeQuery();
            while (rs.next()) {
                ProjectDTO project = fillDTO(rs);
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
    public void export(File selectedFile,ProjectDTO dto) throws FatalException{
        checkObjects(selectedFile);
        checkObjects(dto);
        File dir = new File(dto.getProjectPath());
        if (!dir.exists()) {
            throw new FatalException("Error the project does not exist on the path: " +dir);
        }
        try{
            createTarGz(dir.toString(), selectedFile.getAbsolutePath().concat(".tar.gz"));
        }catch (FatalException e){
            deleteFile(new File(selectedFile.getAbsolutePath().concat(".tar.gz") ) );
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO get(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        PreparedStatement pr;
        ResultSet rs;
        ProjectDTO result;

        try {
            pr = this.dal.prepareStatement(SQL_SELECT_BY_PROJECTID);
            pr.setInt(1, dto.getProjectId());
            rs = pr.executeQuery();
            if(rs.next()){
                result = fillDTO(rs);
            } else {
                throw new FatalException("Project with id : " + dto.getProjectId() + " does not exist");
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
            pr.setInt(1, dto.getProjectId());
            pr.executeUpdate();
        } catch (SQLException e) {
            throw new FatalException("Failed to Delete the project '" + dto.getProjectName() + "' in Database");
        }
        deleteFileSilent(new File(dto.getProjectPath()));
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

    private String rootProject = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator  ;

    public void save(Canvas canvas, UserDTO userDTO) throws FatalException {
        String nameOfProject = ActiveProject.getActiveProject().getProjectName();
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(rootProject +"userid_" +userDTO.getUserId() + File.separator + nameOfProject + File.separator + nameOfProject + ".bin"));
            objectOutputStream.writeObject(canvas);
            objectOutputStream.close();
        }catch (IOException e){
            throw new FatalException("Error while saving the project " + e.getMessage());
        }
    }

    public Canvas loadSavedCanvas(UserDTO userDTO) throws FatalException {
        String nameOfProject = ActiveProject.getActiveProject().getProjectName();
        FileInputStream fileInputStream = null;
        Canvas canvas = null;
        try {
            fileInputStream = new FileInputStream(rootProject +"userid_" +userDTO.getUserId() + File.separator + nameOfProject + File.separator + nameOfProject + ".bin");
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            canvas = (Canvas) in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            throw new FatalException("Error File not found " + e.getMessage());
        } catch (IOException e) {
            throw new FatalException("Error while opening the project " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new FatalException("Error while loading the project " + e.getMessage());
        }
        return canvas;
    }

    //TODO remove
    private void renameFolderProject(File projectName, File NewProjectName) {
        projectName.renameTo(NewProjectName);
    }

    /**
     *
     * @param selectedArchive The archive to import
     * @param projectDTO DTO with the name of the new project once imported
     * @param userDTO Active user
     * @return
     * @throws FatalException
     */
    public ProjectDTO load(File selectedArchive, ProjectDTO projectDTO, UserDTO userDTO) throws FatalException, BizzException {
        checkObjects(projectDTO.getProjectName());
        checkObjects(selectedArchive);
        checkObjects(userDTO);
        String projectName = projectDTO.getProjectName();
        checkString(projectName,"Project Name ");
        Path folderDestination = Paths.get(System.getProperty("user.home") + rootFolder  + File.separator +"userid_" +userDTO.getUserId() + File.separator);
        if (!Files.exists(folderDestination.resolve(projectName))) { // check if the project name is already taken
            try {
                Files.createDirectories(folderDestination); //create user dir if doesn't exists
                Files.createDirectories(folderDestination.resolve("tmp")); //create a temp dir
                Files.createDirectories(folderDestination.resolve(projectName)) ; //create a dir with real project name
                String destination = Utility.unTarFile(selectedArchive, folderDestination.resolve("tmp")); //untar the archive in tmp
                Utility.copy(folderDestination.resolve("tmp"+ File.separator+ destination).toFile(), folderDestination.resolve(projectName).toFile() );
                renameFolderProject(new File(folderDestination.toFile()+File.separator+ File.separator+destination), new File(folderDestination.toString() + File.separator + File.separator+projectName));
                renameFolderProject(new File(folderDestination.toFile()+File.separator+ File.separator+projectName+File.separator+destination+".bin"), new File(folderDestination.toFile()+File.separator+ File.separator+projectName+File.separator+projectName+".bin"));
                Path delFile = Paths.get(folderDestination.resolve("tmp"+File.separator).toString()) ;
                Utility.deleteFile(delFile.toFile());
                projectDTO.setProjectPath(folderDestination.toString()+ File.separator +projectName);
                this.create(projectDTO);
            } catch (IOException e) {
                throw new FatalException("IO exception : Can't find the file to import");
            } catch (FatalException fatale){
                Utility.deleteFileSilent(new File(folderDestination.resolve("tmp").toString()));
                Utility.deleteFileSilent(new File(folderDestination.resolve(projectName).toString()));
                throw new BizzException("Please select a .tar.gz file");
            }
        } else {
            throw new FatalException("A project with this name already exists");
        }
        return projectDTO;
    }
}
