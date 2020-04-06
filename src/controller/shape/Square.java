package controller.shape;

import utilities.exceptions.FatalException;

public class Square extends Rectangle{
    public Square(Coordinates orginCoordinates, Coordinates endCoordinates, String shapeThickness, int id) throws FatalException {
        super(orginCoordinates, endCoordinates, shapeThickness, id);
    }

    public Square(Coordinates orginCoordinates, int size, String shapeThickness, int id) throws FatalException {
        this(orginCoordinates, new Coordinates(orginCoordinates.getX()+size,orginCoordinates.getY()+size), shapeThickness, id);
    }
}
