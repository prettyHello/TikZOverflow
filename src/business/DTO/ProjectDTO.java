package business.DTO;


public class ProjectDTO {
    private int projectOwnerId  ;
    private String projectName ;
    private String projectReference  ;
    private String projectPath ;
    private String createDate ;
    private String modificationDate ;

    public String getProjectName() {
        return projectName;
    }

    public ProjectDTO setProjectName(String projectName) {
        this.projectName=projectName ;
        return  this;
    }

    public Integer getProjectOwnerId() {
        return projectOwnerId;
    }

    public ProjectDTO setProjectOwnerId(int projectOwnerId) {
        this.projectOwnerId=projectOwnerId;
        return  this;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public ProjectDTO setProjectPath(String projectPath) {
        this.projectPath=projectPath;
        return  this;
    }

    public String getCreateDate() {
        return createDate;
    }

    public ProjectDTO setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public ProjectDTO setModificationDate(String modificationDate) {
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
