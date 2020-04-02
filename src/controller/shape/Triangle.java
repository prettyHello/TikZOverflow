package controller.shape;

import utilities.Utility;
import utilities.exceptions.FatalException;

import java.util.ArrayList;

public class Triangle extends Shape {
    private Coordinates originPoint;
    private Coordinates secondPoint;
    private Coordinates thirdPoint;

    /**
     * Simple triangle, only set one point, who is the bottom left of
     * a isosceles right triangle, of 1 width/height
     *
     * @param originPoint
     */
    public Triangle(Coordinates originPoint, int id) {
        super(true, false, id);
        Utility.checkObjects(originPoint);
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
    public Triangle(Coordinates pt1, Coordinates pt2, Coordinates pt3, int id) {
        super(true, false, id);
        Utility.checkObjects(pt1, pt2, pt3);
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
    public Triangle(boolean draw, boolean fill, Color drawColor, Color fillColor, Coordinates originPoint, Coordinates secondPoint, Coordinates thirdPoint, int id) throws FatalException {
        super(draw, fill, drawColor, fillColor, id);
        Utility.checkObjects(originPoint, secondPoint, thirdPoint);
        this.originPoint = originPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
    }

    public String print() {
        String returnValue = super.print();
        returnValue += this.originPoint.print() + "-- " + this.secondPoint.print() + "-- " + this.thirdPoint.print() + "-- cycle;";
        return returnValue;
    }

    public ArrayList<Coordinates> getPoints() {
        ArrayList<Coordinates> points = new ArrayList<>();
        points.add(this.originPoint);
        points.add(this.secondPoint);
        points.add(this.thirdPoint);

        return points;
    }
}
