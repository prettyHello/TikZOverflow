package business.shape;

import exceptions.FatalException;
import utilities.Utility;

import java.util.Iterator;

/**
 * eg : \draw (0,0) circle [radius=1.5];
 */
public class Circle extends Shape{
    private Coordinates coordinates = null;
    private float radius = 1;

    /**
     *
     * @param coordinates
     * @param radius
     */
    public Circle(Coordinates coordinates, float radius) {
        super(true,false);
        this.coordinates = coordinates;
        this.radius = radius;
    }

    /**
     *
     * @param draw
     * @param fill
     * @param fillColor
     * @param drawColor
     * @param coordinates
     * @param radius
     */
    public Circle(boolean draw, boolean fill, Color fillColor, Color drawColor, Coordinates coordinates, float radius) {
        super(draw, fill, fillColor, drawColor);
        this.coordinates = coordinates;
        this.radius = radius;
    }

    /**
     * @return The line of Tikz code representing this object.
     * /!\ Print always add an exta " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print(){
        String returnValue = super.print();
        returnValue += this.coordinates.print()+"circle [radius="+this.radius+"];";
        return returnValue;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) throws FatalException {
        Utility.checkObject(coordinates);
        this.coordinates = coordinates;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) throws FatalException {
        Utility.checkObject(coordinates);
        this.radius = radius;
    }
}
