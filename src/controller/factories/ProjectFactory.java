package controller.factories;

import controller.ProjectImpl;

/**
 * This interface is meant to be used by the front end or the persistence by giving them the DTO they need
 */
public interface ProjectFactory {
    ProjectImpl createProject();

    ProjectImpl createProject(String projectName);

    ProjectImpl createProject(int project_id, int projectOwnerId, String projectName, String projectPath, String creationDate, String modificationDate);
}
