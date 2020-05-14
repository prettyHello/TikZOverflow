package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.util.ArrayList;
import java.util.List;

public class TriangleDTO extends LabelizableShapeDTO {
    private CoordinatesDTO originPoint;
    private CoordinatesDTO secondPoint;
    private CoordinatesDTO thirdPoint;

    /**
     * Simple triangle, only set one point, who is the bottom left of
     * a isosceles right triangle, of 1 width/height
     *
     * @param originPoint
     */
    public TriangleDTO(CoordinatesDTO originPoint, String shapeThickness, int id) {
        super(true, false, shapeThickness, id);
        ControllerUtility.checkObjects(originPoint);
        this.originPoint = originPoint;
        this.secondPoint = new CoordinatesDTO(originPoint.getX() + 1, originPoint.getY());
        this.thirdPoint = new CoordinatesDTO(originPoint.getX(), originPoint.getY() + 1);
    }

    /**
     * Creates a triangle from three given points and an id
     *
     * @param pt1 the first point of the triangle
     * @param pt2 the second point of the triangle
     * @param pt3 the third point of the triangle
     * @param id  the id of the shape
     */
    public TriangleDTO(CoordinatesDTO pt1, CoordinatesDTO pt2, CoordinatesDTO pt3, String shapeThickness, int id) {
        super(true, false, shapeThickness, id);
        ControllerUtility.checkObjects(pt1, pt2, pt3);
        this.originPoint = pt1;
        this.secondPoint = pt2;
        this.thirdPoint = pt3;
    }

    /**
     * @param draw      Is the shape have a outer line, can be combined with fill.
     * @param fill      Is the shape filled with a color, can be combined with draw.
     * @param fillColor Color to fill the shape with, color list in Color enum.
     * @param drawColor Outer line color, color list in Color enum.
     */
    public TriangleDTO(boolean draw, boolean fill, Color drawColor, Color fillColor, String shapeThickness, CoordinatesDTO originPoint, CoordinatesDTO secondPoint, CoordinatesDTO thirdPoint, int id) throws FatalException {
        super(draw, fill, drawColor, fillColor, shapeThickness, id);
        ControllerUtility.checkObjects(originPoint, secondPoint, thirdPoint);
        this.originPoint = originPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
    }

    /**
     * Copy constructor
     * @param other the Triangle to copy
     * @param newId the id to assign this newly created ShapeDTO
     */
    public TriangleDTO(TriangleDTO other, int newId){
        super(other.isDraw(), other.isFill(), other.getDrawColor(), other.getFillColor(), other.getShapeThicknessKey(), newId);
        this.originPoint = other.getOriginPoint();
        this.secondPoint = other.getSecondPoint();
        this.thirdPoint = other.getThirdPoint();
        this.setLabel(other.getLabel());
    }

    public String print() {
        String returnValue = super.print();
        returnValue += this.originPoint.print() + "-- " + this.secondPoint.print() + "-- " + this.thirdPoint.print() + "-- cycle";
        returnValue += super.printLabel();
        returnValue += ";";
        return returnValue;
    }

    public List<CoordinatesDTO> getPoints() {
        List<CoordinatesDTO> points = new ArrayList<>();
        points.add(this.originPoint);
        points.add(this.secondPoint);
        points.add(this.thirdPoint);

        return points;
    }

    @Override
    public CoordinatesDTO calcLabelOffset() {
        CoordinatesDTO o1 = originPoint, o2 = secondPoint, o3 = thirdPoint;
        CoordinatesDTO centroid = new CoordinatesDTO((o1.getX() + o2.getX() + o3.getX()) / 3, (o1.getY() + o2.getY() + o3.getY()) / 3);
        return new CoordinatesDTO(centroid.getX() - o1.getX(), centroid.getY() - o1.getY());
    }

    public CoordinatesDTO getOriginPoint() {
        return originPoint;
    }

    public CoordinatesDTO getSecondPoint() {
        return secondPoint;
    }

    public CoordinatesDTO getThirdPoint() {
        return thirdPoint;
    }

    public void setOriginPoint(CoordinatesDTO originPoint) {
        this.originPoint = originPoint;
    }

    public void setSecondPoint(CoordinatesDTO secondPoint) {
        this.secondPoint = secondPoint;
    }

    public void setThirdPoint(CoordinatesDTO thirdPoint) {
        this.thirdPoint = thirdPoint;
    }

    @Override
    public CoordinatesDTO getCoordinates() {
        return this.getOriginPoint();
    }
}
