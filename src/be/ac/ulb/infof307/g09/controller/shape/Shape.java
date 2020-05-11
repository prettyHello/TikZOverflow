package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.Serializable;

/**
 * Shape is the representation of a line of tikz code, eg :
 * \draw[thick,rounded corners=8pt] (0,0) -- (0,2) -- (1,3.25)
 * -- (2,2) -- (2,0) -- (0,2) -- (2,2) -- (0,0) -- (2,0);
 * In order to avoid confusion with an actual line (the shape line, who is called a path in tikz, still following ?)
 * , it's called a shape here.
 * <p>
 * Since it's an abstract class it's only intended to extend actual shape class, like Path.java,
 * that should be used to represent the code line above.
 */
public abstract class Shape implements Serializable {

    private boolean draw;
    private boolean fill;
    private Color fillColor = Color.BLACK;
    private Color drawColor = Color.BLACK;
    private String shapeThicknessKey;
    private double shapeThicknessValue;
    private int id;

    /**
     * @param draw              Is the shape have a outer line, can be combined with fill.
     * @param fill              Is the shape filled with a color, can be combined with draw.
     * @param shapeThicknessKey Thickness starting value.
     */
    public Shape(boolean draw, boolean fill, String shapeThicknessKey, int id) {
        this.draw = draw;
        this.fill = fill;
        this.shapeThicknessKey = shapeThicknessKey;
        this.shapeThicknessValue = Thickness.valueOf(shapeThicknessKey).thicknessValue();
        this.id = id;
    }

    /**
     * @param draw              Is the shape have a outer line, can be combined with fill.
     * @param fill              Is the shape filled with a color, can be combined with draw.
     * @param fillColor         Color to fill the shape with, color list in Color enum.
     * @param drawColor         Outer line color, color list in Color enum.
     * @param shapeThicknessKey Thickness starting value.
     */
    public Shape(boolean draw, boolean fill, Color drawColor, Color fillColor, String shapeThicknessKey, int id) throws FatalException {
        this.draw = draw;
        this.fill = fill;

        if (fill) {
            ControllerUtility.checkObjects(fillColor);
            this.fillColor = fillColor;
        }
        if (draw) {
            ControllerUtility.checkObjects(drawColor);
            this.drawColor = drawColor;
        }
        this.shapeThicknessKey = shapeThicknessKey;
        this.shapeThicknessValue = Thickness.valueOf(shapeThicknessKey).thicknessValue();
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj instanceof Shape) {
            Shape ptr = (Shape) obj;
            retVal = ptr.getId() == this.id;
        }
        return retVal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
    }

    public String getShapeThicknessKey() {
        return this.shapeThicknessKey;
    }

    public String getShapeThicknessKeyFormatted() {
        return shapeThicknessKey.toLowerCase().replace("_", " ");
    }

    public Shape setShapeThicknessKey(String shapeThicknessKey) {
        this.shapeThicknessKey = shapeThicknessKey.toUpperCase();
        this.shapeThicknessValue = Thickness.valueOf(shapeThicknessKey).thicknessValue();
        return this;
    }

    /**
     * @return the Coordinates that is considered to be the shape's anchor point
     */
    abstract public Coordinates getCoordinates();

    public double getShapeThicknessValue() {
        return shapeThicknessValue;
    }

    public String print() {
        String returnValue = "";
        if (this.fill && this.draw) {
            returnValue += "\\filldraw";
            returnValue += "[fill=" + this.fillColor.getValue() + ", draw=" + this.drawColor.getValue() + ", " + this.getShapeThicknessKeyFormatted() + "] ";
        } else {
            if (this.fill) {
                returnValue += "\\fill";
                returnValue += "[fill=" + this.fillColor.getValue() + ", " + this.getShapeThicknessKeyFormatted() + "] ";
            }
            if (this.draw) {
                returnValue += "\\draw";
                returnValue += "[draw=" + this.drawColor.getValue() + ", " + this.getShapeThicknessKeyFormatted() + "] ";
            }
        }
        return returnValue;
    }
}