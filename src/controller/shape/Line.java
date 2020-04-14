package controller.shape;

import utilities.exceptions.FatalException;

public class Line extends Path {

    public Line(Coordinates origin, Coordinates end, int id) throws FatalException {
        super(origin, end, id,false,false);
    }

    public Line(Coordinates origin, Coordinates end, Color drawColor, int id) throws FatalException {
        super(origin, end,false,false, drawColor, id);
    }
}
