package controller.Canvas;

import controller.DTO.ProjectDTO;
import controller.ProjectImpl;

/**
 * Singleton that contains the only active project.
 */
public class ActiveProject {

    /**
     * Container for the connected user
     */
    private static ProjectDTO activeProject = null;

    public static void setActiveProject(ProjectDTO project) {
        activeProject = project;
    }

    public static void deleteActiveProject() {
        activeProject = null;
    }

    public static ProjectDTO getActiveProject() {
        return activeProject;
    }
}
