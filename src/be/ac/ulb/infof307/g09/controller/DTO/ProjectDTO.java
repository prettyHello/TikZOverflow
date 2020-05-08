package be.ac.ulb.infof307.g09.controller.DTO;

/**
 * The project DTO contains all the data related to the project and is serializable
 * the project DTO travels between the mvc layers of the application
 */
public interface ProjectDTO {
    int getProjectId();

    void setProjectId(int projectId);

    int getProjectOwnerId();

    void setProjectOwnerId(int projectOwnerId);

    String getProjectName();

    void setProjectName(String projectName);

    String getProjectPath();

    void setProjectPath(String projectPath);

    String getCreateDate();

    void setCreateDate(String createDate);

    String getModificationDate();

    void setModificationDate(String modificationDate);

    //TODO
    void setProjectPassword(String password);

    String getProjectPassword();

    int hashCode();

    boolean equals(Object obj);

    String getHash();

    void setHash(String hash);
}
