package business.Canvas;

/**
 * Singleton for the canvas that is currently being worked on
 */
public class ActiveCanvas {
    private static CanvasImpl INSTANCE = null;

    public static Canvas getActiveCanvas() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Currently no active canvas set");
        }
        return INSTANCE;
    }

    //TODO used by load project ?
    public static void setActiveCanvas(CanvasImpl canvas) {
        INSTANCE = canvas;
    }

    public static void setNewEmptyCanvas() {
        INSTANCE = new CanvasImpl();
    }

    public static void deleteActiveCanvas() {
        INSTANCE = null;
    }
}
