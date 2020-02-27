package business.DTO;

import java.util.Date;

public class ProjectDTO {
    private String projectName;
    private String projectReference;
    private String projectPath;
    private Date createDate;
    private Date modificationDate;

    public String getProjectName() {
        return projectName;
    }

    public ProjectDTO setProjectName(String projectName) {
        this.projectName = projectName;
        return  this;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public ProjectDTO setProjectPath(String projectPath) {
        this.projectPath = projectPath;
        return  this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public ProjectDTO setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public ProjectDTO setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
        return  this;
    }

    public String getProjectReference() {
        return projectReference;
    }

    public ProjectDTO setProjectReference(String projectReference) {
        this.projectReference = projectReference;
        return  this;
    }
}
