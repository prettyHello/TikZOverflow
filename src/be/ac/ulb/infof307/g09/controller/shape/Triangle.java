package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.util.ArrayList;
import java.util.List;

public class Triangle extends LabelizableShape {
    private Coordinates originPoint;
    private Coordinates secondPoint;
    private Coordinates thirdPoint;

    /**
     * Simple triangle, only set one point, who is the bottom left of
     * a isosceles right triangle, of 1 width/height
     *
     * @param originPoint
     */
    public Triangle(Coordinates originPoint, Thickness thickness, int id) {
        super(true, false, thickness, id);
        ControllerUtility.checkObjects(originPoint);
        this.originPoint = originPoint;
        this.secondPoint = new Coordinates(originPoint.getX() + 1, originPoint.getY());
        this.thirdPoint = new Coordinates(originPoint.getX(), originPoint.getY() + 1);
    }

    /**
     * Creates a triangle from three given points and an id
     *
     * @param pt1 the first point of the triangle
     * @param pt2 the second point of the triangle
     * @param pt3 the third point of the triangle
     * @param id  the id of the shape
     */
    public Triangle(Coordinates pt1, Coordinates pt2, Coordinates pt3, Thickness thickness, int id) {
        super(true, false, thickness, id);
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
    public Triangle(boolean draw, boolean fill, Color drawColor, Color fillColor, Thickness thickness, Coordinates originPoint, Coordinates secondPoint, Coordinates thirdPoint, int id) throws FatalException {
        super(draw, fill, drawColor, fillColor, thickness, id);
        ControllerUtility.checkObjects(originPoint, secondPoint, thirdPoint);
        this.originPoint = originPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
    }

    /**
     * Copy constructor
     *
     * @param other the Triangle to copy
     * @param newId the id to assign this newly created Shape
     */
    public Triangle(Triangle other, int newId) {
        super(other.isDraw(), other.isFill(), other.getDrawColor(), other.getFillColor(), other.getThickness(), newId);
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

    public List<Coordinates> getPoints() {
        List<Coordinates> points = new ArrayList<>();
        points.add(this.originPoint);
        points.add(this.secondPoint);
        points.add(this.thirdPoint);

        return points;
    }

    @Override
    public Coordinates calcLabelOffset() {
        Coordinates o1 = originPoint, o2 = secondPoint, o3 = thirdPoint;
        Coordinates centroid = new Coordinates((o1.getX() + o2.getX() + o3.getX()) / 3, (o1.getY() + o2.getY() + o3.getY()) / 3);
        return new Coordinates(centroid.getX() - o1.getX(), centroid.getY() - o1.getY());
    }

    public Coordinates getOriginPoint() {
        return originPoint;
    }

    public Coordinates getSecondPoint() {
        return secondPoint;
    }

    public Coordinates getThirdPoint() {
        return thirdPoint;
    }

    public void setOriginPoint(Coordinates originPoint) {
        this.originPoint = originPoint;
    }

    public void setSecondPoint(Coordinates secondPoint) {
        this.secondPoint = secondPoint;
    }

    public void setThirdPoint(Coordinates thirdPoint) {
        this.thirdPoint = thirdPoint;
    }

    @Override
    public Coordinates getCoordinates() {
        return this.getOriginPoint();
    }
}
