package be.ac.ulb.infof307.g09.controller.Canvas;

import be.ac.ulb.infof307.g09.controller.shape.Color;
import be.ac.ulb.infof307.g09.controller.shape.Shape;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.Serializable;
import java.util.List;

public interface Canvas extends Serializable {

    /**
     * Getter for the shapes on the canvas
     *
     * @return the list of shapes on the canvas
     */
    List<Shape> getShapes();

    /**
     * Returns the shape in the canvas with the specified id
     *
     * @param id the id of the shape to find
     * @return the shape
     * @throws IllegalArgumentException if the canvas does not contain the shape
     */
    Shape getShapeById(int id) throws IllegalArgumentException;

    /**
     * Adds a shape to the canvas
     *
     * @param shape the shape to add
     */
    void addShape(Shape shape) throws FatalException;

    /**
     * update a shape to the canvas
     *
     * @param shape the shape to update
     */
    void updateShape(Shape shape) throws FatalException;

    /**
     * Change the draw color of a shape
     *
     * @param id        id of the shape
     * @param drawColor color to draw whit
     */
    void changeShapeDrawColor(int id, Color drawColor);

    /**
     * Change the fill color of a shape
     *
     * @param id        id of the shape
     * @param fillColor color to fill whit
     */
    void changeShapeFillColor(int id, Color fillColor);

    /**
     * Sets the label of the shape with that id
     * @param shapeId the id of the shape
     * @param label the label to set
     */
    void setShapeLabel(int shapeId, String label, Color labelColor);

    /**
     * Get the id for the next shape
     *
     * @return id for the next shape
     */
    int getIdForNewShape();

    /**
     * Removes a shape from the canvas
     *
     * @param shape the shape to remove
     */
    void rmShape(Shape shape);

    /**
     * Removes a shape from the canvas
     *
     * @param id the id of the shape to remove
     */
    void rmShapeById(int id);

    /**
     * Generates the TikZ code that describes this canvas
     *
     * @return the TikZ code
     */
    String toTikZ();

    /**
     * Empty the current shapes in the canvas
     *
     */
    void clear();
}
