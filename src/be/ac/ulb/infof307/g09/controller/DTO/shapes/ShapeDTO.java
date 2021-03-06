package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.Serializable;

/**
 * ShapeDTO is the representation of a line of tikz code, eg :
 * \draw[thick,rounded corners=8pt] (0,0) -- (0,2) -- (1,3.25)
 * -- (2,2) -- (2,0) -- (0,2) -- (2,2) -- (0,0) -- (2,0);
 * In order to avoid confusion with an actual line (the shape line, who is called a path in tikz, still following ?)
 * , it's called a shape here.
 * <p>
 * Since it's an abstract class it's only intended to extend actual shape class, like Path.java,
 * that should be used to represent the code line above.
 */
public abstract class ShapeDTO implements Serializable {

    private boolean draw;
    private boolean fill;
    private ColorDTO fillColor = ColorDTO.BLACK;
    private ColorDTO drawColor = ColorDTO.BLACK;
    private Thickness thickness;
    private int id;

    /**
     * @param draw      Is the shape have a outer line, can be combined with fill.
     * @param fill      Is the shape filled with a color, can be combined with draw.
     * @param thickness Thickness starting value.
     */
    public ShapeDTO(boolean draw, boolean fill, Thickness thickness, int id) {
        this.draw = draw;
        this.fill = fill;
        this.thickness = thickness;
        this.id = id;
    }

    /**
     * @param draw              Is the shape have a outer line, can be combined with fill.
     * @param fill              Is the shape filled with a color, can be combined with draw.
     * @param fillColor         Color to fill the shape with, color list in Color enum.
     * @param drawColor         Outer line color, color list in Color enum.
     * @param thickness Thickness starting value.
     */
    public ShapeDTO(boolean draw, boolean fill, ColorDTO drawColor, ColorDTO fillColor, Thickness thickness, int id) throws FatalException {
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
        this.thickness = thickness;
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj instanceof ShapeDTO) {
            ShapeDTO ptr = (ShapeDTO) obj;
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

    public ColorDTO getFillColor() {
        return fillColor;
    }

    public void setFillColor(ColorDTO fillColor) {
        this.fillColor = fillColor;
    }

    public ColorDTO getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(ColorDTO drawColor) {
        this.drawColor = drawColor;
    }

    public Thickness getThickness() {
        return this.thickness;
    }

    public String getThicknessFormatted() {
        return thickness.name().toLowerCase().replace("_", " ");
    }

    public void setThickness(Thickness thicknessKey) {
        this.thickness = thicknessKey;
    }

    /**
     * @return the Coordinates that is considered to be the shape's anchor point
     */
    public abstract CoordinatesDTO getCoordinates();

    public String print() {
        String returnValue = "";
        if (this.fill && this.draw) {
            returnValue += "\\filldraw";
            returnValue += "[fill=" + this.fillColor.getValue() + ", draw=" + this.drawColor.getValue() + ", " + this.getThicknessFormatted() + "] ";
        } else {
            if (this.fill) {
                returnValue += "\\fill";
                returnValue += "[fill=" + this.fillColor.getValue() + ", " + this.getThicknessFormatted() + "] ";
            }
            if (this.draw) {
                returnValue += "\\draw";
                returnValue += "[draw=" + this.drawColor.getValue() + ", " + this.getThicknessFormatted() + "] ";
            }
        }
        return returnValue;
    }
}