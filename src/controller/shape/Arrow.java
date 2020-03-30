package controller.shape;

import utilities.exceptions.FatalException;

import java.util.Iterator;

public class Arrow extends Path {

    public Arrow(Coordinates origin, Coordinates end, int id) throws FatalException {
        super(origin, end, id, false, true);
    }

    public String print() {
        String returnValue = "\\draw ["+super.getDrawColor().value+",->] ";
        Iterator<Coordinates> iterator = this.getCoordinatesIterator();
        returnValue += iterator.next().print();
        while (iterator.hasNext()) {
            returnValue += "-- ";
            returnValue += iterator.next().print();
        }
        returnValue += ";";
        return returnValue;
    }
}
