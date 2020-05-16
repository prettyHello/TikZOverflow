package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.controller.ControllerUtility;
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
public class PathDTO extends ShapeDTO {
    private List<CoordinatesDTO> pathPoints = null;
    private boolean arrowStart = false;
    private boolean arrowEnd = false;

    /**
     * Simple black path, no arrow.
     *
     * @param pathPoints List of points the path goes trough.
     * @throws FatalException
     */
    public PathDTO(ArrayList<CoordinatesDTO> pathPoints, Thickness thickness, int id) throws FatalException {
        super(true, false, thickness, id);
        ControllerUtility.checkObjects(pathPoints);
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
    public PathDTO(CoordinatesDTO origin, CoordinatesDTO end, Thickness thickness, int id, boolean arrowStart, boolean arrowEnd) throws FatalException {
        super(true, false, thickness, id);
        this.arrowStart = arrowStart;
        this.arrowEnd = arrowEnd;
        ArrayList<CoordinatesDTO> pathPoints = new ArrayList<>();
        pathPoints.add(new CoordinatesDTO(origin));
        pathPoints.add(new CoordinatesDTO(end));
        ControllerUtility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;
    }

    /**
     * Personalised path with multiple points, with arrows and specified color.
     *
     * @param pathPoints
     * @param drawColor
     * @throws FatalException
     */
    public PathDTO(ArrayList<CoordinatesDTO> pathPoints, ColorDTO drawColor, Thickness thickness, int id) throws FatalException {
        super(true, false, drawColor, ColorDTO.WHITE, thickness, id);
        ControllerUtility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;
    }

    /**
     * Personalised path with multiple points, with arrows and specified color.
     *
     * @param origin
     * @param end
     * @param drawColor
     * @throws FatalException
     */
    public PathDTO(CoordinatesDTO origin, CoordinatesDTO end, ColorDTO drawColor, Thickness thickness, int id) throws FatalException {
        super(true, false, drawColor, ColorDTO.WHITE, thickness, id);
        ArrayList<CoordinatesDTO> pathPoints = new ArrayList<>();
        pathPoints.add(new CoordinatesDTO(origin));
        pathPoints.add(new CoordinatesDTO(end));
        ControllerUtility.checkObjects(pathPoints);
        this.pathPoints = pathPoints;
    }

    public Iterator<CoordinatesDTO> getCoordinatesIterator() {
        return pathPoints.iterator();
    }

    /**
     * @return The Tikz code for these coordinates, intended to use in other print function of shapes, like rectangle.
     * /!\ Print always add an extra " " empty character at the end, no need to add one if concatenating multiple Print result.
     */
    public String print() {
        StringBuilder returnValue = new StringBuilder("\\draw [" + super.getDrawColor().getValue() + ", " + super.getThicknessFormatted() + "] ");
        Iterator<CoordinatesDTO> iterator = this.getCoordinatesIterator();
        returnValue.append(iterator.next().print());
        while (iterator.hasNext()) {
            returnValue.append("-- ");
            returnValue.append(iterator.next().print());
        }
        returnValue.append(";");
        return returnValue.toString();
    }

    public void addCoordinates(CoordinatesDTO coordinates) throws FatalException {
        ControllerUtility.checkObjects(coordinates);
        this.pathPoints.add(coordinates);
    }

    public void rmCoordinates(CoordinatesDTO coordinates) throws FatalException {
        ControllerUtility.checkObjects(coordinates);
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
    public List<CoordinatesDTO> getPathPoints() {
        return this.pathPoints;
    }

    public CoordinatesDTO getStartCoordinates() {
        return this.getPathPoints().get(0);
    }

    public CoordinatesDTO getEndCoordinates() {
        List<CoordinatesDTO> points = getPathPoints();
        return points.get(points.size() - 1);
    }

    public void setStartCoordinates(CoordinatesDTO val) {
        this.getPathPoints().set(0, val);
    }

    public void setEndCoordinates(CoordinatesDTO val) {
        List<CoordinatesDTO> points = this.getPathPoints();
        points.set(points.size() - 1, val);
    }

    @Override
    public CoordinatesDTO getCoordinates() {
        return this.getStartCoordinates();
    }
}
