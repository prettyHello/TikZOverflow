package controller.shape;

import utilities.exceptions.FatalException;

public class Line extends Path {

    public Line(Coordinates origin, Coordinates end,  String shapeThickness,int id) throws FatalException {
        super(origin, end, shapeThickness, id,false,false);
    }

    public Line(Coordinates origin, Coordinates end, Color drawColor, String shapeThickness, int id) throws FatalException {
        super(origin, end,false,false, drawColor, shapeThickness, id);
    }
}
