package controller.shape;

import utilities.exceptions.FatalException;

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
    public Square(Coordinates originCoordinates, Coordinates endCoordinates, int id) throws FatalException {
        super(originCoordinates, endCoordinates, id);
        this.size = endCoordinates.getX() - originCoordinates.getX();
    }

    /**
     * Create a rectangle from its origin coordinates, adding the size of the side.
     * @param originCoordinates
     * @param size
     * @param id
     * @throws FatalException
     */
    public Square(Coordinates originCoordinates, double size, int id) throws FatalException {
        this(originCoordinates, new Coordinates(originCoordinates.getX()+size,originCoordinates.getY()+size), id);
        this.size = size;
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
