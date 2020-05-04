package be.ac.ulb.infof307.g09.controller;


import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;
import be.ac.ulb.infof307.g09.controller.shape.Shape;

/**
 * The project DTO contains all the data related to the project and is serializable
 * the project DTO travels between the mvc layers of the application
 */
public class ProjectImpl implements ProjectDTO {
    private int projectId;
    private int projectOwnerId;
    private String projectName;
    private String projectPath;
    private String createDate;
    private String modificationDate;
    //TODO
    private String projectPassword = "";

    public ProjectImpl() {
    }

    public ProjectImpl(String projectName) {
        this.projectName = projectName;
    }

    public ProjectImpl(int projectId, int projectOwnerId, String projectName, String projectPath, String createDate, String modificationDate) {
        this.projectId = projectId;
        this.projectOwnerId = projectOwnerId;
        this.projectName = projectName;
        this.projectPath = projectPath;
        this.createDate = createDate;
        this.modificationDate = modificationDate;
    }

    @Override
    public int getProjectId() {
        return projectId;
    }

    @Override
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public int getProjectOwnerId() {
        return projectOwnerId;
    }

    @Override
    public void setProjectOwnerId(int projectOwnerId) {
        this.projectOwnerId = projectOwnerId;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getProjectPath() {
        return projectPath;
    }

    @Override
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    @Override
    public String getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String getModificationDate() {
        return modificationDate;
    }

    @Override
    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    //TODO
    @Override
    public void setProjectPassword(String password) {
        this.projectPassword = password;
    }

    @Override
    public String getProjectPassword() {
        return projectPassword;
    }

    @Override
    public int hashCode() {
        return this.projectId;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj instanceof Shape){
            Shape ptr = (Shape) obj;
            retVal = ptr.getId() == this.projectId;
        }
        return retVal;
    }
}
