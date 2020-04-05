package controller.factories;

import controller.DTO.ProjectDTO;
//TODO CHECK IF CORRECT AFTER REFACTOR
/**
 * {@inheritDoc}
 */
public class ProjectFactoryImpl implements ProjectFactory {
    @Override
    public ProjectDTO createProject() {
        return new ProjectDTO();
    }

    @Override
    public ProjectDTO createProject(String projectName) {
        return new ProjectDTO(projectName);
    }

    @Override
    public ProjectDTO createProject(int project_id, int projectOwnerId, String projectName, String projectReference, String projectPath, String creationDate, String modificationDate) {
        return new ProjectDTO(project_id, projectOwnerId, projectName, projectReference, projectPath, creationDate, modificationDate);
    }
}
