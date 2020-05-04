package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.controller.Canvas.ActiveCanvas;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveProject;
import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.Canvas.CanvasImpl;
import be.ac.ulb.infof307.g09.controller.Crypto;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.ProjectImpl;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.controller.Utility;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static be.ac.ulb.infof307.g09.controller.Utility.*;


/**
 * {@inheritDoc}
 */
public class ProjectDAOImpl implements ProjectDAO {
    private final String rootFolder = File.separator + "ProjectTikZ" + File.separator;

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
            ActiveCanvas.setNewCanvas();
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
            be.ac.ulb.infof307.g09.model.Utility.createTarGz(dir.toString(), selectedFile.getAbsolutePath().concat(".tar.gz"));
        }catch (FatalException e){
            be.ac.ulb.infof307.g09.model.Utility.deleteFile(new File(selectedFile.getAbsolutePath().concat(".tar.gz") ) );
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
        be.ac.ulb.infof307.g09.model.Utility.deleteFileSilent(new File(dto.getProjectPath()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ProjectDTO obj) {
        throw new UnsupportedOperationException("Not implemented yet"); // can't update a project yet
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Canvas canvas, ProjectDTO dto) throws FatalException {
        Utility.checkObjects(canvas);
        Utility.checkObjects(dto);
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dto.getProjectPath()+ File.separator + dto.getProjectName() + ".bin"));
            objectOutputStream.writeObject(canvas);
            objectOutputStream.close();
            //TODO
            Crypto.encryptDirectory(dto.getProjectPassword(), dto.getProjectPath());
        }catch (IOException e){
            throw new FatalException("Error while saving the project " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Canvas loadSavedCanvas(ProjectDTO dto, String password) throws FatalException {
        FileInputStream fileInputStream = null;
        Canvas canvas = null;
        try {
            //TODO
            Crypto.decryptDirectory(password, dto.getProjectPath());
            fileInputStream = new FileInputStream(dto.getProjectPath()+ File.separator + dto.getProjectName() + ".bin");
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            canvas = (Canvas) in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            //There is no canvas in this project, this can happen in two case:
            // 1) the user might have quit without ever saving <= which used to create a bug
            // 2) he deleted the file manually
            return new CanvasImpl();
        } catch (IOException e) {
            throw new FatalException("Error while opening the project " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new FatalException("Error while loading the project " + e.getMessage());
        }
        return canvas;
    }

    private void renameFolderProject(File projectName, File NewProjectName) {
        projectName.renameTo(NewProjectName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
                String destination = be.ac.ulb.infof307.g09.model.Utility.untarfile(selectedArchive, folderDestination.resolve("tmp")); //untar the archive in tmp
                be.ac.ulb.infof307.g09.model.Utility.copy(folderDestination.resolve("tmp"+ File.separator+ destination).toFile(), folderDestination.resolve(projectName).toFile() );
                renameFolderProject(new File(folderDestination.toFile()+File.separator+ File.separator+destination), new File(folderDestination.toString() + File.separator + File.separator+projectName));
                renameFolderProject(new File(folderDestination.toFile()+File.separator+ File.separator+projectName+File.separator+destination+".bin"), new File(folderDestination.toFile()+File.separator+ File.separator+projectName+File.separator+projectName+".bin"));
                Path delFile = Paths.get(folderDestination.resolve("tmp"+File.separator).toString()) ;
                be.ac.ulb.infof307.g09.model.Utility.deleteFile(delFile.toFile());
                projectDTO.setProjectPath(folderDestination.toString()+ File.separator +projectName);
                this.create(projectDTO);
            } catch (IOException e) {
                throw new FatalException("IO exception : Can't find the file to import");
            } catch (FatalException fatale){
                be.ac.ulb.infof307.g09.model.Utility.deleteFileSilent(new File(folderDestination.resolve("tmp").toString()));
                be.ac.ulb.infof307.g09.model.Utility.deleteFileSilent(new File(folderDestination.resolve(projectName).toString()));
                throw new BizzException("Please select a .tar.gz file not empty");
            }
        } else {
            throw new FatalException("A project with this name already exists");
        }
        return projectDTO;
    }
}
