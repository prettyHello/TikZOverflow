package business.shape;

import exceptions.FatalException;
import utilities.Utility;

/**
 * eg : \draw (0,0) rectangle (1,1);
 * 0,0 is the origin coordinate, 1,1 is the end coordinate.
 */
public class Rectangle extends Shape{
    private Coordinates orginCoordinates = null;
    private Coordinates endCoordinates = null;

    /**
     * Default rectangle, drawn with a black line.
     * @param orginCoordinates
     * @param endCoordinates
     * @throws FatalException
     */
    public Rectangle(Coordinates orginCoordinates,Coordinates endCoordinates) throws FatalException {
        super(true,false);
        Utility.checkObject(orginCoordinates);
        Utility.checkObject(endCoordinates);
        this.orginCoordinates = orginCoordinates;
        this.endCoordinates = endCoordinates;
    }

    /**
     * Personalised rectangle.
     * @param orginCoordinates
     * @param endCoordinates
     * @param draw      Is the shape have a outer line, can be combined with fill.
     * @param fill      Is the shape filled with a color, can be combined with draw.
     * @param fillColor Color to fill the shape with, color list in Color enum.
     * @param drawColor Outer line color, color list in Color enum.
     */
    public Rectangle(boolean draw, boolean fill, Color drawColor, Color fillColor, Coordinates orginCoordinates, Coordinates endCoordinates) throws FatalException {
        super(draw, fill, drawColor, fillColor);
        Utility.checkObject(orginCoordinates);
        Utility.checkObject(endCoordinates);
        this.orginCoordinates = orginCoordinates;
        this.endCoordinates = endCoordinates;
    }

    /**
     * @return The line of Tikz code representing this object.
     * /!\ Print always add an exta " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print(){
        String returnValue = super.print();
        returnValue += this.orginCoordinates.print()+"rectangle "+this.endCoordinates.print()+";";
        return returnValue;
    }
}
