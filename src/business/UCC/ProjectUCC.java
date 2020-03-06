package business.UCC;

import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import exceptions.BizzException;
import exceptions.FatalException;

import java.io.File;
import java.nio.file.Path;

public interface ProjectUCC {

    public void renameFolderProject(File projectName, File NewProjectName);

    public String setProjectName (String popupMessage) throws BizzException;

    public ProjectDTO getProjectDTO(String projectName, Path folderDestination, int userId);

    }
