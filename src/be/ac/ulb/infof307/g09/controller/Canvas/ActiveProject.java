package be.ac.ulb.infof307.g09.controller.Canvas;

import be.ac.ulb.infof307.g09.controller.DTO.ProjectDTO;

/**
 * Class that contains the only active project.
 */
public class ActiveProject {

    /**
     * Container for the connected user
     */
    private static ProjectDTO activeProject = null;

    /**
     * private constructor to prevent instantiation
     */
    private ActiveProject() {
    }

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
