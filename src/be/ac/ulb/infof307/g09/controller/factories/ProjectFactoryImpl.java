package be.ac.ulb.infof307.g09.controller.factories;

import be.ac.ulb.infof307.g09.controller.ProjectImpl;

/**
 * {@inheritDoc}
 */
public class ProjectFactoryImpl implements ProjectFactory {
    @Override
    public ProjectImpl createProject() {
        return new ProjectImpl();
    }

    @Override
    public ProjectImpl createProject(String projectName) {
        return new ProjectImpl(projectName);
    }

    @Override
    public ProjectImpl createProject(int projectId, int projectOwnerId, String projectName, String projectPath, String creationDate, String modificationDate, String hash) {
        return new ProjectImpl(projectId, projectOwnerId, projectName, projectPath, creationDate, modificationDate,hash);
    }
}
