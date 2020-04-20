package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.utilities.Utility;
import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

public class Square extends Rectangle{
    private double size;

    /**
     * Create a default rectangle from origin and end coordinates.
     *
     * @param originCoordinates
     * @param endCoordinates
     * @param id
     * @throws FatalException
     */
    public Square(Coordinates originCoordinates, Coordinates endCoordinates, String shapeThickness, int id) throws FatalException {
        super(originCoordinates, endCoordinates, shapeThickness, id);
        this.size = endCoordinates.getX() - originCoordinates.getX();
    }

    /**
     * Create a rectangle from its origin coordinates, adding the size of the side.
     * @param originCoordinates
     * @param size
     * @param id
     * @throws FatalException
     */
    public Square(Coordinates originCoordinates, double size, String shapeThickness, int id) throws FatalException {
        this(originCoordinates, new Coordinates(originCoordinates.getX()+size,originCoordinates.getY()+size), shapeThickness, id);
        this.size = size;
    }

    /**
     * Personalised square.
     *
     * @param originCoordinates
     * @param endCoordinates
     * @param draw             Is the shape have a outer line, can be combined with fill.
     * @param fill             Is the shape filled with a color, can be combined with draw.
     * @param fillColor        Color to fill the shape with, color list in Color enum.
     * @param drawColor        Outer line color, color list in Color enum.
     */
    public Square (boolean draw, boolean fill, Color drawColor, Color fillColor, Coordinates originCoordinates, Coordinates endCoordinates, String shapeThickness, int id) throws FatalException {
        super(draw, fill, drawColor, fillColor,  shapeThickness, originCoordinates, endCoordinates, id);
        Utility.checkObjects(originCoordinates, endCoordinates);
        this.size = endCoordinates.getX() - originCoordinates.getX();
    }

    /**
     * @return the side size of this square.
     */
    public double getSize() {
        return this.size;
    }

    /**
     * Set the side size of this square.
     * @param size
     */
    public void setSize(double size) {
        this.size = size;
    }
}
