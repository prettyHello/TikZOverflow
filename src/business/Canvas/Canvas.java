package business.Canvas;

import java.util.List;

public interface Canvas {

    /**
     * Getter for the shapes on the canvas
     *
     * @return the list of shapes on the canvas
     */
    List<Shape> getShapes();

    /**
     * Adds a shape to the canvas
     *
     * @param shape the shape to add
     */
    void addShape(Shape shape);

    /**
     * Removes a shape from the canvas
     *
     * @param shape the shape to remove
     */
    void rmShape(Shape shape);

    /**
     * Generates the TikZ code that describes this canvas
     *
     * @return the TikZ code
     */
    String toTikZ();
}
