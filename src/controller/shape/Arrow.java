package controller.shape;

import utilities.exceptions.FatalException;

import java.util.Iterator;

public class Arrow extends Path {

    public Arrow(Coordinates origin, Coordinates end, String shapeThickness, int id) throws FatalException {
        super(origin, end, shapeThickness, id, false, true);
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
