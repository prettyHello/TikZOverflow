package business.shape;

import exceptions.FatalException;
import utilities.Utility;

import java.util.Iterator;

public class Triangle extends Shape {
    private Coordinates originPoint;
    private Coordinates secondPoint;
    private Coordinates thirdPoint;

    /**
     * Simple triangle, only set one point, who is the bottom left of
     * a isosceles right triangle, of 1 width/height
     * @param originPoint
     */
    public Triangle(Coordinates originPoint) {
        super(true, false);
        Utility.checkObject(originPoint);
        this.originPoint = originPoint;
        this.secondPoint = new Coordinates(originPoint.getX()+1,originPoint.getY());
        this.thirdPoint = new Coordinates(originPoint.getX(),originPoint.getY()+1);
    }

    /**
     * @param draw      Is the shape have a outer line, can be combined with fill.
     * @param fill      Is the shape filled with a color, can be combined with draw.
     * @param fillColor Color to fill the shape with, color list in Color enum.
     * @param drawColor Outer line color, color list in Color enum.
     */
    public Triangle(boolean draw, boolean fill, Color drawColor, Color fillColor, Coordinates originPoint, Coordinates secondPoint, Coordinates thirdPoint) throws FatalException {
        super(draw, fill, drawColor, fillColor);
        Utility.checkObject(originPoint);
        Utility.checkObject(secondPoint);
        Utility.checkObject(thirdPoint);
        this.originPoint = originPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
    }

    public String print(){
        String returnValue = super.print();
        returnValue += this.originPoint.print()+"-- "+this.secondPoint.print()+"-- "+this.thirdPoint.print()+"-- cycle;";
        return returnValue;
    }

}
