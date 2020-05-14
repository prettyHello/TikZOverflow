package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.util.Iterator;

public class ArrowDTO extends PathDTO {

    public ArrowDTO(CoordinatesDTO origin, CoordinatesDTO end, String shapeThickness, int id) throws FatalException {
        super(origin, end, shapeThickness, id, false, true);
    }

    public ArrowDTO(CoordinatesDTO origin, CoordinatesDTO end, Color drawColor, String shapeThickness, int id) throws FatalException {
        super(origin, end,false,true, drawColor, shapeThickness, id);
    }

    /**
     * Copy-constructor
     * @param other the Arrow to copy
     * @param newId the id to give to the newly created Arraow
     */
    public ArrowDTO(ArrowDTO other, int newId){
        super(new CoordinatesDTO(other.getStartCoordinates()), new CoordinatesDTO(other.getEndCoordinates()), other.getShapeThicknessKey(), newId, other.isArrowStart(), other.isArrowEnd());
        this.setDraw(other.isDraw());
        this.setFill(other.isFill());
        this.setFillColor(other.getFillColor());
        this.setDrawColor(other.getDrawColor());
    }

    public String print() {
        StringBuilder returnValue = new StringBuilder("\\draw [" + super.getDrawColor().getValue() + ",->, "+super.getShapeThicknessKeyFormatted()+"] ");
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
