package business.factories;

import business.DTO.ProjectDTO;

public interface ProjectFactory {
    ProjectDTO createProject();

    ProjectDTO createProject(String projectName);

    ProjectDTO createProject(int project_id, int projectOwnerId, String projectName, String projectReference, String projectPath, String creationDate, String modificationDate);
}
