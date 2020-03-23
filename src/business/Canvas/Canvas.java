package business.Canvas;

import business.shape.Shape;

import java.io.Serializable;
import java.util.List;

public interface Canvas extends Serializable {

    /**
     * Getter for the width of the canvas
     */
    int getWidth();

    /**
     * Getter for the height of the canvas
     */
    int getHeight();

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
