package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.exceptions.FatalException;

public class LineDTO extends PathDTO {

    public LineDTO(CoordinatesDTO origin, CoordinatesDTO end, Thickness thickness, int id) throws FatalException {
        super(origin, end, thickness, id,false,false);
    }

    public LineDTO(CoordinatesDTO origin, CoordinatesDTO end, ColorDTO drawColor, Thickness thickness, int id) throws FatalException {
        super(origin, end,false,false, drawColor, thickness, id);
    }

    /**
     * Copy constructor
     * @param other the Line to copy
     * @param newId the id to assign to the newly created line
     */
    public LineDTO(LineDTO other, int newId){
        super(new CoordinatesDTO(other.getStartCoordinates()), new CoordinatesDTO(other.getEndCoordinates()), other.getThickness(), newId, other.isArrowStart(), other.isArrowEnd());
        this.setDraw(other.isDraw());
        this.setFill(other.isFill());
        this.setFillColor(other.getFillColor());
        this.setDrawColor(other.getDrawColor());
    }
}
