package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

/**
 * eg : \draw (0,0) circle [radius=1.5];
 */
public class CircleDTO extends LabelizableShapeDTO {
    private CoordinatesDTO coordinates;
    private float radius = 1;

    /**
     * @param coordinates
     * @param radius
     */
    public CircleDTO(CoordinatesDTO coordinates, float radius, String shapeThickness, int id) throws FatalException {
        super(true, false, shapeThickness, id);
        if (radius <= 0) {
            throw new BizzException("Radius is negative or null");
        }
        this.coordinates = coordinates;
        this.radius = radius;
    }

    /**
     * @param draw
     * @param fill
     * @param fillColor
     * @param drawColor
     * @param coordinates
     * @param radius
     */
    public CircleDTO(boolean draw, boolean fill, Color drawColor, Color fillColor, String shapeThickness, CoordinatesDTO coordinates, float radius, int id) throws FatalException, BizzException {
        super(draw, fill, drawColor, fillColor, shapeThickness, id);
        ControllerUtility.checkObjects(coordinates);
        if (radius <= 0) {
            throw new BizzException("Radius is negative or null");
        }
        this.coordinates = coordinates;
        this.radius = radius;
    }

    /**
     * Copy constructor
     * @param other the circle to copy
     * @param newId the id to give to the newly created circle
     */
    public CircleDTO(CircleDTO other, int newId){
        super(other.isDraw(), other.isFill(), other.getDrawColor(), other.getFillColor(), other.getShapeThicknessKey(), newId);
        this.coordinates = new CoordinatesDTO(other.getCoordinates().getX(), other.getCoordinates().getY());
        this.radius = other.getRadius();
        this.setLabel(other.getLabel());
    }

    /**
     * @return The line of Tikz code representing this object.
     * /!\ Print always add an extra " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print() {
        String returnValue = super.print();
        returnValue += this.coordinates.print() + "circle [radius=" + this.radius + "] ";
        returnValue += super.printLabel();
        returnValue += ";";
        return returnValue;
    }

    @Override
    public CoordinatesDTO getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesDTO coordinates) throws FatalException {
        ControllerUtility.checkObjects(coordinates);
        this.coordinates = coordinates;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) throws BizzException {
        if (radius <= 0) {
            throw new BizzException("Radius is negative or null");
        }
        this.radius = radius;
    }

    @Override
    public CoordinatesDTO calcLabelOffset() {
        return new CoordinatesDTO(0, 0);
    }
}
