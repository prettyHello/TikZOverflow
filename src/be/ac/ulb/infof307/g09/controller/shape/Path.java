package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.Utility;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * /!\ Only used in the package, use Arrow or line for the displaying a path. (subject to change later)
 * Only support straight path for now
 * eg : \draw (-1.5,0) -- (1.5,0) -- (0,-1.5) -- (0,1.5);
 * A path can have arrow on both sides;
 * eg : \draw [<->]
 * for now, we only support one type of arrow.
 */
public class Path extends Shape {
    private List<Coordinates> pathPoints = null;
    private boolean arrowStart = false;
    private boolean arrowEnd = false;

    /**
     * Simple black path, no arrow.
     *
     * @param pathPoints List of points the path goes trough.
     * @throws FatalException
     */
    public Path(ArrayList<Coordinates> pathPoints, String shapeThickness, int id) throws FatalException {
        super(true, false, shapeThickness, id);
        Utility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;
    }

    /**
     * Personalised path with only 2 points, with arrows and specified color.
     *
     * @param origin
     * @param end
     * @param id
     * @throws FatalException
     */
    public Path(Coordinates origin, Coordinates end, String shapeThickness, int id, boolean arrowStart, boolean arrowEnd) throws FatalException {
        super(true, false, shapeThickness, id);
        this.arrowStart = arrowStart;
        this.arrowEnd = arrowEnd;
        ArrayList<Coordinates> pathPoints = new ArrayList<>();
        pathPoints.add(new Coordinates(origin));
        pathPoints.add(new Coordinates(end));
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
    public Path(ArrayList<Coordinates> pathPoints, boolean arrowStart, boolean arrowEnd, Color drawColor, String shapeThickness, int id) throws FatalException {
        super(true, false, drawColor, Color.WHITE, shapeThickness, id);
        Utility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;
    }

    /**
     * Personalised path with multiple points, with arrows and specified color.
     *
     * @param origin
     * @param end
     * @param arrowStart
     * @param arrowEnd
     * @param drawColor
     * @throws FatalException
     */
    public Path(Coordinates origin, Coordinates end, boolean arrowStart, boolean arrowEnd, Color drawColor, String shapeThickness, int id) throws FatalException {
        super(true, false, drawColor, Color.WHITE, shapeThickness, id);
        ArrayList<Coordinates> pathPoints = new ArrayList<>();
        pathPoints.add(new Coordinates(origin));
        pathPoints.add(new Coordinates(end));
        Utility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;
    }

    public Iterator<Coordinates> getCoordinatesIterator() {
        return pathPoints.iterator();
    }

    /**
     * @return The Tikz code for these coordinates, intended to use in other print function of shapes, like rectangle.
     * /!\ Print always add an extra " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print() {
        StringBuilder returnValue = new StringBuilder("\\draw [" + super.getDrawColor().value + ", " + super.getShapeThicknessKeyFormatted() + "] ");
        Iterator<Coordinates> iterator = this.getCoordinatesIterator();
        returnValue.append(iterator.next().print());
        while (iterator.hasNext()) {
            returnValue.append("-- ");
            returnValue.append(iterator.next().print());
        }
        returnValue.append(";");
        return returnValue.toString();
    }

    public void addCoordinates(Coordinates coordinates) throws FatalException {
        Utility.checkObjects(coordinates);
        this.pathPoints.add(coordinates);
    }

    public void rmCoordinates(Coordinates coordinates) throws FatalException {
        Utility.checkObjects(coordinates);
        this.pathPoints.remove(coordinates);
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
    public List<Coordinates> getPathPoints() {
        return this.pathPoints;
    }

    public Coordinates getStartCoordinates() {
        return this.getPathPoints().get(0);
    }

    public Coordinates getEndCoordinates() {
        List<Coordinates> points = getPathPoints();
        return points.get(points.size() - 1);
    }

    public void setStartCoordinates(Coordinates val) {
        this.getPathPoints().set(0, val);
    }

    public void setEndCoordinates(Coordinates val) {
        List<Coordinates> points = this.getPathPoints();
        points.set(points.size() - 1, val);
    }

    @Override
    public Coordinates getCoordinates() {
        return this.getStartCoordinates();
    }
}
