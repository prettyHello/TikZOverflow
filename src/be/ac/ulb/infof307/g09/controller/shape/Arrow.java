package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.util.Iterator;

public class Arrow extends Path {

    public Arrow(Coordinates origin, Coordinates end, Thickness thickness, int id) throws FatalException {
        super(origin, end, thickness, id, false, true);
    }

    public Arrow(Coordinates origin, Coordinates end, Color drawColor, Thickness thickness, int id) throws FatalException {
        super(origin, end, false, true, drawColor, thickness, id);
    }

    /**
     * Copy-constructor
     *
     * @param other the Arrow to copy
     * @param newId the id to give to the newly created Arraow
     */
    public Arrow(Arrow other, int newId) {
        super(new Coordinates(other.getStartCoordinates()), new Coordinates(other.getEndCoordinates()), other.getThickness(), newId, other.isArrowStart(), other.isArrowEnd());
        this.setDraw(other.isDraw());
        this.setFill(other.isFill());
        this.setFillColor(other.getFillColor());
        this.setDrawColor(other.getDrawColor());
    }

    public String print() {
        StringBuilder returnValue = new StringBuilder("\\draw [" + super.getDrawColor().getValue() + ",->, " + super.getThicknessFormatted() + "] ");
        Iterator<Coordinates> iterator = this.getCoordinatesIterator();
        returnValue.append(iterator.next().print());
        while (iterator.hasNext()) {
            returnValue.append("-- ");
            returnValue.append(iterator.next().print());
        }
        returnValue.append(";");
        return returnValue.toString();
    }
}
