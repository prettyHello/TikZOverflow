package business.shape;

import exceptions.FatalException;
import utilities.Utility;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * TODO, implementation is like shape for now, but when looking at tikz code, we can see that while using the draw command, the color definition is probably different for path, might need to check that when going from tikz to canvas.
 * Only support straight path for now
 * eg : \draw (-1.5,0) -- (1.5,0) -- (0,-1.5) -- (0,1.5);
 * <p>
 * A path can have arrow on both sides;
 * eg : \draw [<->]
 * for now, we only support one type of arrow.
 */
public class Path extends Shape {
    private ArrayList<Coordinates> pathPoints = null;
    private boolean arrowStart = false;
    private boolean arrowEnd = false;

    /**
     * Simple black path, no arrow.
     *
     * @param pathPoints List of points the path goes trough.
     * @throws FatalException
     */
    public Path(ArrayList<Coordinates> pathPoints, int id) throws FatalException {
        super(true, false, id);
        Utility.checkObject(pathPoints);
        this.pathPoints = pathPoints;
    }

    /**
     * TODO benja, add the damn doc when you do something.
     * @param origin
     * @param end
     * @param id
     * @throws FatalException
     */
    public Path(Coordinates origin, Coordinates end, int id,boolean arrowStart,boolean arrowEnd) throws FatalException {
        super(true, false, id);
        this.arrowStart = arrowStart;
        this.arrowEnd = arrowEnd;
        ArrayList<Coordinates> pathPoints = new ArrayList<>();
        pathPoints.add(origin);
        pathPoints.add(end);
        Utility.checkObject(pathPoints);
        this.pathPoints = pathPoints;

    }


    /**
     * Personalised path, with arrow and specified color.
     *
     * @param pathPoints
     * @param arrowStart
     * @param arrowEnd
     * @param drawColor
     * @throws FatalException
     */
    public Path(ArrayList<Coordinates> pathPoints, boolean arrowStart, boolean arrowEnd, Color drawColor, int id) throws FatalException {
        super(true, false, Color.WHITE, drawColor, id);
        Utility.checkObject(pathPoints);
        this.pathPoints = pathPoints;
    }

    public Iterator<Coordinates> getCoordinatesIterator() {
        return pathPoints.iterator();
    }

    /**
     * TODO fix arrow
     *
     * @return The Tikz code for these coordinates, intended to use in other print function of shapes, like rectangle.
     * /!\ Print always add an exta " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print() {
        String returnValue = "\\draw ["+super.getDrawColor().value+"] ";
        Iterator<Coordinates> iterator = this.getCoordinatesIterator();
        returnValue += iterator.next().print();
        while (iterator.hasNext()) {
            returnValue += "-- ";
            returnValue += iterator.next().print();
        }
        returnValue += ";";
        return returnValue;
    }

    public void addCoordinates(Coordinates coordinates) throws FatalException {
        Utility.checkObject(coordinates);
        this.pathPoints.add(coordinates);
    }

    public void rmCoordinates(Coordinates coordinates) throws FatalException {
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
