package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.util.Iterator;

public class ArrowDTO extends PathDTO {

    public ArrowDTO(CoordinatesDTO origin, CoordinatesDTO end, Thickness thickness, int id) throws FatalException {
        super(origin, end, thickness, id, false, true);
    }

    public ArrowDTO(CoordinatesDTO origin, CoordinatesDTO end, ColorDTO drawColor, Thickness thickness, int id) throws FatalException {
        super(origin, end,false,true, drawColor, thickness, id);
    }

    /**
     * Copy-constructor
     * @param other the Arrow to copy
     * @param newId the id to give to the newly created Arraow
     */
    public ArrowDTO(ArrowDTO other, int newId){
        super(new CoordinatesDTO(other.getStartCoordinates()), new CoordinatesDTO(other.getEndCoordinates()), other.getThickness(), newId, other.isArrowStart(), other.isArrowEnd());
        this.setDraw(other.isDraw());
        this.setFill(other.isFill());
        this.setFillColor(other.getFillColor());
        this.setDrawColor(other.getDrawColor());
    }

    /**
     * @return The Tikz code for these coordinates, intended to use in other print function of shapes, like rectangle.
     * /!\ Print always add an extra " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print() {
        StringBuilder returnValue = new StringBuilder("\\draw [" + super.getDrawColor().getValue() + ",->, "+super.getThicknessFormatted()+"] ");
        Iterator<CoordinatesDTO> iterator = this.getCoordinatesIterator();
        returnValue.append(iterator.next().print());
        while (iterator.hasNext()) {
            returnValue.append("-- ");
            returnValue.append(iterator.next().print());
        }
        returnValue.append(";");
        return returnValue.toString();
    }
}
