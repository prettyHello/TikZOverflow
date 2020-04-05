package controller.factories;

import controller.DTO.ProjectDTO;
//TODO CHECK IF CORRECT AFTER REFACTOR

/**
 * This interface is meant to be used by the front end or the persistence by giving them the DTO they need
 */
public interface ProjectFactory {
    ProjectDTO createProject();

    ProjectDTO createProject(String projectName);

    ProjectDTO createProject(int project_id, int projectOwnerId, String projectName, String projectReference, String projectPath, String creationDate, String modificationDate);
}
