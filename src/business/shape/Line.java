package business.shape;

import exceptions.FatalException;

import java.util.ArrayList;

public class Line extends Path {

    public Line(Coordinates origin, Coordinates end) throws FatalException {
        super(origin, end);
    }
}
