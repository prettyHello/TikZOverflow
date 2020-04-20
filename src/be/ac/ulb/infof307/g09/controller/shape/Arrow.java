package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

import java.util.Iterator;

public class Arrow extends Path {

    public Arrow(Coordinates origin, Coordinates end, String shapeThickness, int id) throws FatalException {
        super(origin, end, shapeThickness, id, false, true);
    }

    public Arrow(Coordinates origin, Coordinates end, Color drawColor, String shapeThickness, int id) throws FatalException {
        super(origin, end,false,true, drawColor, shapeThickness, id);
    }

    public String print() {
        StringBuilder returnValue = new StringBuilder("\\draw [" + super.getDrawColor().value + ",-> , "+super.getShapeThicknessKey()+"] ");
        Iterator<Coordinates> iterator = this.getCoordinatesIterator();
        returnValue.append(iterator.next().print());
        while (iterator.hasNext()) {
            returnValue.append("-- ");
            returnValue.append(iterator.next().print());
        }
        returnValue.append(";");
        return returnValue.toString();
    }
}
