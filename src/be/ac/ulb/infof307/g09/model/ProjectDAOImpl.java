package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.Canvas.CanvasImpl;
import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.ProjectImpl;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.controller.ControllerUtility;
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

import static be.ac.ulb.infof307.g09.controller.ControllerUtility.*;


/**
 * {@inheritDoc}
 */
public class ProjectDAOImpl implements ProjectDAO {
    private final String rootFolder = File.separator + "ProjectTikZ" + File.separator;

    private static final String SQL_INSERT_PROJECT = "INSERT INTO projects(project_owner_id, name, path, creation_date, modification_date, hash ) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_LAST_INSERTED_PROJECTD = "SELECT * FROM projects WHERE project_id = last_insert_rowid()";
    private static final String SQL_SELECT_PROJECT = "SELECT * FROM projects WHERE project_owner_id = ?";
    private static final String SQL_SELECT_BY_PROJECTID = "SELECT * FROM projects WHERE project_id = ?";
    private static final String SQL_DELETE_PROJECT_OF_USER = "DELETE FROM projects WHERE project_id = ?";
    private static final String SQL_UPDATE_HASH = "UPDATE projects SET hash = ? WHERE project_id = ?";
    private final DALBackEndServices dal;
    private final ProjectFactory projectFactory;


    public ProjectDAOImpl(DALServices dalServices, ProjectFactory projectFactory) {
        this.dal = (DALBackEndServices) dalServices;
        this.projectFactory = projectFactory;
    }



    /**
     * {@inheritDoc}
     */
    public ProjectDTO create(ProjectDTO dto) throws FatalException {
        checkObjects(dto);
        PreparedStatement pr = null;
        ResultSet rs = null;
        ProjectDTO result;
        try {
            pr = dal.prepareStatement(SQL_INSERT_PROJECT);
            pr.setInt(1, dto.getProjectOwnerId());
            pr.setString(2, dto.getProjectName());
            pr.setString(3, dto.getProjectPath());
            pr.setString(4, dto.getCreateDate());
            pr.setString(5, dto.getModificationDate());
            pr.setString(6, dto.getHash());
            pr.executeUpdate();

            pr = this.dal.prepareStatement(SQL_LAST_INSERTED_PROJECTD);
            rs = pr.executeQuery();
            if(rs.next()){
                result = fillDTO(rs);
            } else {
                throw new FatalException("Error while inserting the new project: unable to get next id");
            }
            Files.createDirectories(Paths.get(result.getProjectPath()));
            return result;
        } catch (SQLException e) {
            throw new FatalException("Project already exists");
        } catch (IOException e){
            throw new FatalException("Couldn't create folder");
        }finally {
            ModelUtility.closeStatements(pr,rs);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ProjectDTO> getOwnedProjects(UserDTO dto) throws FatalException {
        checkObjects(dto);
        PreparedStatement pr = null;
        ResultSet rs = null;
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
        }finally {
            ModelUtility.closeStatements(pr,rs);
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
            ModelUtility.createTarGz(dir.toString(), selectedFile.getAbsolutePath().concat(".tar.gz"));
        }catch (FatalException e){
            ModelUtility.deleteFile(new File(selectedFile.getAbsolutePath().concat(".tar.gz") ) );
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO get(ProjectDTO dto) throws FatalException, BizzException {
        checkObjects(dto);
        PreparedStatement pr = null;
        ResultSet rs = null;
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
        }finally {
            ModelUtility.closeStatements(pr,rs);
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
        ModelUtility.deleteFileSilent(new File(dto.getProjectPath()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ProjectDTO dto) {
        PreparedStatement pr = null;
        try {
            pr = this.dal.prepareStatement(SQL_UPDATE_HASH);
            pr.setString(1, dto.getHash());
            pr.setInt(2, dto.getProjectId());
            pr.executeUpdate();
        } catch (SQLException e) {
            throw new FatalException("SQLException in projectDAOImpl: impossible to update the hash to the database");
        }finally {
            ModelUtility.closeStatements(pr,null);
        }
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
        projectDTO.setHash(rs.getString("hash"));
        return projectDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO save(Canvas canvas, ProjectDTO dto) throws FatalException {
        ControllerUtility.checkObjects(canvas);
        ControllerUtility.checkObjects(dto);
        ObjectOutputStream objectOutputStream = null;
        try{
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(dto.getProjectPath()+ File.separator + dto.getProjectName() + ".bin"));
            objectOutputStream.writeObject(canvas);
            objectOutputStream.close();
            File uncryptedFile = new File(dto.getProjectPath() + File.separator + dto.getProjectName() + ".bin");
            dto.setHash(Crypto.hashingFile(uncryptedFile));
            Crypto.encryptDirectory(dto.getProjectPassword(), dto.getProjectPath());
            update(dto);
            return dto;
        }catch (IOException e){
            throw new FatalException("Error while saving the project " + e.getMessage());
        }finally {
            if(objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    throw new FatalException("Couldn't close statement : " + e.getMessage());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Canvas loadSavedCanvas(ProjectDTO dto, String password) throws FatalException {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        Canvas canvas = null;
        dto = this.get(dto);

        try {
            Crypto.decryptDirectory(password, dto.getProjectPath());
            File uncryptedZip = new File(dto.getProjectPath() + File.separator + dto.getProjectName() + ".bin");
            if(uncryptedZip.exists()){ //if the file was manually deleted or not saved (user clicked cancel or close app) he will create a blank canvas so nothing to check
                if(!dto.getHash().equals(Crypto.hashingFile(uncryptedZip))){
                    throw new BizzException("The project was modified by an other program:\n Hashes are different.");
                }
            }
            fileInputStream = new FileInputStream(dto.getProjectPath()+ File.separator + dto.getProjectName() + ".bin");
            objectInputStream = new ObjectInputStream(fileInputStream);
            canvas = (Canvas) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            //There is no canvas in this project, this can happen in two case:
            // 1) the user might have quit without ever saving <= which used to create a bug
            // 2) he deleted the file manually
            return new CanvasImpl();
        } catch (IOException e) {
            throw new FatalException("Error while opening the project " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new FatalException("Error while loading the project " + e.getMessage());
        }finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new FatalException("Couldn't close statement : " + e.getMessage());
                }
            }
            if(objectInputStream != null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    throw new FatalException("Couldn't close statement : " + e.getMessage());
                }
            }
        }
        return canvas;
    }

    private void renameFolderProject(File projectName, File newProjectName) {
        projectName.renameTo(newProjectName);
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
                String destination = ModelUtility.untarfile(selectedArchive, folderDestination.resolve("tmp")); //untar the archive in tmp
                ModelUtility.copy(folderDestination.resolve("tmp"+ File.separator+ destination).toFile(), folderDestination.resolve(projectName).toFile() );
                renameFolderProject(new File(folderDestination.toFile()+File.separator+ File.separator+destination), new File(folderDestination.toString() + File.separator + File.separator+projectName));
                renameFolderProject(new File(folderDestination.toFile()+File.separator+ File.separator+projectName+File.separator+destination+".bin.enc"), new File(folderDestination.toFile()+File.separator+ File.separator+projectName+File.separator+projectName+".bin.enc"));

                //Create the hash before deleting all the files
                folderDestination.resolve("tmp"+ File.separator+ destination).toString();
                try{
                    Crypto.decryptDirectory(projectDTO.getProjectPassword(),folderDestination.resolve("tmp"+ File.separator+ destination).toString());
                }catch (BizzException e){
                    ModelUtility.deleteFileSilent(new File(folderDestination.resolve("tmp").toString()));
                    ModelUtility.deleteFileSilent(new File(folderDestination.resolve(projectName).toString()));
                    throw e;
                }

                File uncryptedFile = new File(folderDestination.resolve("tmp"+ File.separator+ destination+ File.separator+ destination +".bin").toString());
                projectDTO.setHash(Crypto.hashingFile(uncryptedFile));

                Path delFile = Paths.get(folderDestination.resolve("tmp"+File.separator).toString()) ;
                ModelUtility.deleteFile(delFile.toFile());
                projectDTO.setProjectPath(folderDestination.toString()+ File.separator +projectName);
                this.create(projectDTO);
            } catch (IOException e) {
                throw new FatalException("IO exception : Can't find the file to import");
            } catch (FatalException fatale){
                ModelUtility.deleteFileSilent(new File(folderDestination.resolve("tmp").toString()));
                ModelUtility.deleteFileSilent(new File(folderDestination.resolve(projectName).toString()));
                throw new BizzException("Please select a .tar.gz file not empty");
            }
        } else {
            throw new FatalException("A project with this name already exists");
        }
        return projectDTO;
    }
}
