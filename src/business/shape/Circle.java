package business.shape;

import exceptions.FatalException;
import utilities.Utility;

public class Circle extends Shape{
    private Coordinates coordinates = null;
    private float radius = 1;

    public Circle(Coordinates coordinates, float radius) {
        this.coordinates = coordinates;
        this.radius = radius;
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
