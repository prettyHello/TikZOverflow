package business.shape;

import exceptions.FatalException;

public class Square extends Rectangle{
    public Square(Coordinates orginCoordinates, Coordinates endCoordinates) throws FatalException {
        super(orginCoordinates, endCoordinates);
    }

    public Square(Coordinates orginCoordinates, int size) throws FatalException {
        this(orginCoordinates, new Coordinates(orginCoordinates.getX()+size,orginCoordinates.getY()+size));
    }
}
