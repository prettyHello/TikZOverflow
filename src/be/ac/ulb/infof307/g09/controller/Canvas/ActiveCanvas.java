package be.ac.ulb.infof307.g09.controller.Canvas;

import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

/**
 * Singleton for the canvas that is currently being worked on
 */
//TODO why is canvas not a field in ProjectDTO (and thus just part of active project?)
public class ActiveCanvas {
    private static Canvas INSTANCE = null;

    public static Canvas getActiveCanvas() {
        if (INSTANCE == null) {
            throw new FatalException("Currently no active canvas set");
        }
        return INSTANCE;
    }

    public static void setActiveCanvas(Canvas canvas) {
        INSTANCE = canvas;
    }

    public static void setNewCanvas(int width, int height) {
        INSTANCE = new CanvasImpl();
    }

    public static void deleteActiveCanvas() {
        INSTANCE = null;
    }
}
