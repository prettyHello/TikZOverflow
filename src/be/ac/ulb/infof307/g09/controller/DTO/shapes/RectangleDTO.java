package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

/**
 * eg : \draw (0,0) rectangle (1,1);
 * 0,0 is the origin coordinate, 1,1 is the end coordinate.
 */
public class RectangleDTO extends LabelizableShapeDTO {
    private CoordinatesDTO originCoordinates = null;
    private CoordinatesDTO endCoordinates = null;


    /**
     * Default rectangle, drawn with a black line.
     *
     * @param originCoordinates the first defining corner of the rectangle
     * @param endCoordinates    the second defining corner of the rectangle
     */
    public RectangleDTO(CoordinatesDTO originCoordinates, CoordinatesDTO endCoordinates, String shapeThickness, int id) {
        super(true, false, shapeThickness, id);
        ControllerUtility.checkObjects(originCoordinates, endCoordinates);
        this.originCoordinates = originCoordinates;
        this.endCoordinates = endCoordinates;
    }

    /**
     * Personalised rectangle.
     *
     * @param originCoordinates the first defining corner of the rectangle
     * @param endCoordinates    the second defining corner of the rectangle
     * @param draw              Is the shape have a outer line, can be combined with fill.
     * @param fill              Is the shape filled with a color, can be combined with draw.
     * @param fillColor         Color to fill the shape with, color list in Color enum.
     * @param drawColor         Outer line color, color list in Color enum.
     */
    public RectangleDTO(boolean draw, boolean fill, ColorDTO drawColor, ColorDTO fillColor, String shapeThickness, CoordinatesDTO originCoordinates, CoordinatesDTO endCoordinates, int id) throws FatalException {
        super(draw, fill, drawColor, fillColor, shapeThickness, id);
        ControllerUtility.checkObjects(originCoordinates, endCoordinates);
        this.originCoordinates = originCoordinates;
        this.endCoordinates = endCoordinates;
    }

    /**
     * @return The line of Tikz code representing this object.
     * /!\ Print always add an extra " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print() {
        String returnValue = super.print();
        returnValue += this.originCoordinates.print() + "rectangle " + this.endCoordinates.print();
        returnValue += super.printLabel();
        returnValue += ";";
        return returnValue;
    }

    /**
     * @return the origin coordinates of the rectangle.
     */
    public CoordinatesDTO getOriginCoordinates() {
        return this.originCoordinates;
    }

    /**
     * @return the end coordinates of the rectangle.
     */
    public CoordinatesDTO getEndCoordinates() {
        return this.endCoordinates;
    }

    public void setOriginCoordinates(CoordinatesDTO originCoordinates) {
        this.originCoordinates = originCoordinates;
    }

    public void setEndCoordinates(CoordinatesDTO endCoordinates) {
        this.endCoordinates = endCoordinates;
    }

    @Override
    public CoordinatesDTO calcLabelOffset() {
        double xDiff = endCoordinates.getX() - originCoordinates.getX();
        double yDiff = endCoordinates.getY() - originCoordinates.getY();
        return new CoordinatesDTO(xDiff / 2, yDiff / 2);
    }

    @Override
    public CoordinatesDTO getCoordinates() {
        return this.getOriginCoordinates();
    }
}
