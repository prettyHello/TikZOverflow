package controller.shape;

import utilities.Utility;
import utilities.exceptions.FatalException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * /!\ Only used in the package, use Arrow or line for the displaying a path. (subject to change later)
 * Only support straight path for now
 * eg : \draw (-1.5,0) -- (1.5,0) -- (0,-1.5) -- (0,1.5);
 * A path can have arrow on both sides;
 * eg : \draw [<->]
 * for now, we only support one type of arrow.
 */
 class Path extends Shape {
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
        Utility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;
    }

    /**
     * Personalised path with only 2 points, with arrows and specified color.
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
        Utility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;

    }


    /**
     * Personalised path with multiple points, with arrows and specified color.
     *
     * @param pathPoints
     * @param arrowStart
     * @param arrowEnd
     * @param drawColor
     * @throws FatalException
     */
    public Path(ArrayList<Coordinates> pathPoints, boolean arrowStart, boolean arrowEnd, Color drawColor, int id) throws FatalException {
        super(true, false, drawColor, Color.WHITE, id);
        Utility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;
    }

    public Iterator<Coordinates> getCoordinatesIterator() {
        return pathPoints.iterator();
    }

    /**
     * TODO, not in usage right now, status: it's complicated
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
        Utility.checkObjects(coordinates);
        this.pathPoints.add(coordinates);
    }

    public void rmCoordinates(Coordinates coordinates) throws FatalException {
        Utility.checkObjects(coordinates);
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

    /**
     * @return origin and end of Path.
     */
    public ArrayList<Coordinates> getPathPoints() {
        return this.pathPoints;
    }
}
