package business.shape;

import exceptions.FatalException;
import utilities.Utility;

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

    private boolean draw = true;
    private boolean fill = false;
    private Color fillColor = Color.BLACK;
    private Color drawColor = Color.BLACK;

    /**
     * @param draw Is the shape have a outer line, can be combined with fill.
     * @param fill Is the shape filled with a color, can be combined with draw.
     */
    public Shape(boolean draw, boolean fill) {
        this.draw = draw;
        this.fill = fill;
        this.fillColor = Color.BLACK;
        this.drawColor = Color.BLACK;
    }

    /**
     * @param draw      Is the shape have a outer line, can be combined with fill.
     * @param fill      Is the shape filled with a color, can be combined with draw.
     * @param fillColor Color to fill the shape with, color list in Color enum.
     * @param drawColor Outer line color, color list in Color enum.
     */
    public Shape(boolean draw, boolean fill, Color drawColor, Color fillColor) throws FatalException {
        this.draw = draw;
        this.fill = fill;
        if (draw) {
            Utility.checkObject(fillColor);
            this.fillColor = fillColor;
        }
        if (fill) {
            Utility.checkObject(drawColor);
            this.drawColor = drawColor;
        }
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

    public String print() {
        String returnValue = "";
        if (this.fill && this.draw) {
            returnValue += "\\filldraw";
            returnValue += "[fill=" + this.fillColor.value + ", draw=" + this.drawColor.value + "] ";
        } else {
            if (this.fill) {
                returnValue += "\\fill";
                returnValue += "[fill=" + this.fillColor.value + "] ";
            }
            if (this.draw) {
                returnValue += "\\draw";
                returnValue += "[draw=" + this.drawColor.value + "] ";
            }
        }
        return returnValue;
    }
}