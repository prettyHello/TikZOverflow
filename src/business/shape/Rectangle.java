package business.shape;

import exceptions.FatalException;
import utilities.Utility;

/**
 * Coordinate of a rectangle are is bottom left corner.
 */
public class Rectangle extends Shape{
    private float height = 1;
    private float width = 1;
    private Coordinates coordinates = null;

    public Rectangle(float height, float width, Coordinates coordinates) throws FatalException {
        this.height = height;
        this.width = width;
        Utility.checkObject(coordinates);
        this.coordinates = coordinates;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width){
        this.width = width;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
