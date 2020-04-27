package be.ac.ulb.infof307.g09.controller.factories;

import be.ac.ulb.infof307.g09.controller.ProjectImpl;

/**
 * This interface is meant to be used by the front end or the persistence by giving them the DTO they need
 */
public interface ProjectFactory {
    ProjectImpl createProject();

    ProjectImpl createProject(String projectName);

    ProjectImpl createProject(int projectId, int projectOwnerId, String projectName, String projectPath, String creationDate, String modificationDate);
}
