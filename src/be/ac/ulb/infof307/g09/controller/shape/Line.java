package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.exceptions.FatalException;

public class Line extends Path {

    public Line(Coordinates origin, Coordinates end, Thickness thickness, int id) throws FatalException {
        super(origin, end, thickness, id, false, false);
    }

    public Line(Coordinates origin, Coordinates end, Color drawColor, Thickness thickness, int id) throws FatalException {
        super(origin, end, false, false, drawColor, thickness, id);
    }

    /**
     * Copy constructor
     *
     * @param other the Line to copy
     * @param newId the id to assign to the newly created line
     */
    public Line(Line other, int newId) {
        super(new Coordinates(other.getStartCoordinates()), new Coordinates(other.getEndCoordinates()), other.getThickness(), newId, other.isArrowStart(), other.isArrowEnd());
        this.setDraw(other.isDraw());
        this.setFill(other.isFill());
        this.setFillColor(other.getFillColor());
        this.setDrawColor(other.getDrawColor());
    }
}
