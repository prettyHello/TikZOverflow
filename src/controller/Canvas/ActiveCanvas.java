package controller.Canvas;

/**
 * Singleton for the canvas that is currently being worked on
 */
public class ActiveCanvas {
    private static Canvas INSTANCE = null;

    public static Canvas getActiveCanvas() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Currently no active canvas set");
        }
        return INSTANCE;
    }

    public static void setActiveCanvas(Canvas canvas) {
        INSTANCE = canvas;
    }

    public static void setNewEmptyCanvas(int width, int height) {
        INSTANCE = new CanvasImpl(width, height);
    }

    public static void deleteActiveCanvas() {
        INSTANCE = null;
    }
}
