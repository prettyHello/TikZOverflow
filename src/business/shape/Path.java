package business.shape;

import exceptions.FatalException;
import utilities.Utility;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Only support straight path for now
 * eg : \draw (-1.5,0) -- (1.5,0) -- (0,-1.5) -- (0,1.5);
 *
 * A path can have arrow on both sides;
 * eg : \draw [<->]
 * for now, we only support one type of arrow.
 *
 */
public class Path extends Shape {
    private ArrayList<Coordinates> pathPoints = null;
    private boolean arrowStart = false;
    private boolean arrowEnd = false;

    public Path(ArrayList<Coordinates> pathPoints) throws FatalException {
        Utility.checkObject(pathPoints);
        this.pathPoints = pathPoints;
    }

    public Iterator<Coordinates> getCoordinatesIterator() {
        return pathPoints.iterator();
    }

    public void addCoordinates(Coordinates coordinates)throws FatalException{
        Utility.checkObject(coordinates);
        this.pathPoints.add(coordinates);
    }

    public void rmCoordinates(Coordinates coordinates)throws FatalException{
        Utility.checkObject(coordinates);
        this.pathPoints.remove(coordinates);
        //Todo check if it works like that, 50% it won't, let's pray.
    }

    public boolean isArrowStart() {
        return arrowStart;
    }

    public void setArrowStart(boolean arrowStart) {
        this.arrowStart = arrowStart;
    }

    public boolean isArrowEnd() {
        return arrowEnd;
    }

    public void setArrowEnd(boolean arrowEnd) {
        this.arrowEnd = arrowEnd;
    }
}
