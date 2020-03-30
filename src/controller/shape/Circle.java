package controller.shape;

import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

/**
 * eg : \draw (0,0) circle [radius=1.5];
 */
public class Circle extends Shape {
    private Coordinates coordinates;
    private float radius = 1;

    /**
     * @param coordinates
     * @param radius
     */
    public Circle(Coordinates coordinates, float radius, int id) throws FatalException {
        super(true, false, id);
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
    public Circle(boolean draw, boolean fill, Color drawColor, Color fillColor, Coordinates coordinates, float radius, int id) throws FatalException, BizzException {
        super(draw, fill, drawColor, fillColor, id);
        Utility.checkObject(coordinates);
        if (radius <= 0) {
            throw new BizzException("Radius is negative or null");
        }
        this.coordinates = coordinates;
        this.radius = radius;
    }

    /**
     * @return The line of Tikz code representing this object.
     * /!\ Print always add an extra " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print() {
        String returnValue = super.print();
        returnValue += this.coordinates.print() + "circle [radius=" + this.radius + "];";
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

    public void setRadius(float radius) throws BizzException {
        if (radius <= 0) {
            throw new BizzException("Radius is negative or null");
        }
        this.radius = radius;
    }
}
