package business.shape;

import exceptions.FatalException;

public class Square extends Rectangle{
    public Square(Coordinates orginCoordinates, Coordinates endCoordinates, int id) throws FatalException {
        super(orginCoordinates, endCoordinates, id);
    }

    public Square(Coordinates orginCoordinates, int size, int id) throws FatalException {
        this(orginCoordinates, new Coordinates(orginCoordinates.getX()+size,orginCoordinates.getY()+size), id);
    }
}
