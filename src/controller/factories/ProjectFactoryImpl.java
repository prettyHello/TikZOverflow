package controller.factories;

import controller.ProjectImpl;

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
    public ProjectImpl createProject(int project_id, int projectOwnerId, String projectName, String projectPath, String creationDate, String modificationDate) {
        return new ProjectImpl(project_id, projectOwnerId, projectName, projectPath, creationDate, modificationDate);
    }
}
