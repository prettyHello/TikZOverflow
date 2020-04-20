package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.utilities.Utility;
import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

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
    private String shapeThicknessKey;
    private  double shapeThicknessValue;
    private String label = "";
    private int id;

    /**
     * @param draw Is the shape have a outer line, can be combined with fill.
     * @param fill Is the shape filled with a color, can be combined with draw.
     * @param shapeThicknessKey Thickness starting value.
     */
    public Shape(boolean draw, boolean fill, String shapeThicknessKey, int id) {
        this.draw = draw;
        this.fill = fill;
        this.fillColor = Color.BLACK;
        this.drawColor = Color.BLACK;
        setShapeThicknessKey(shapeThicknessKey);
        this.id = id;
    }



    /**
     * @param draw      Is the shape have a outer line, can be combined with fill.
     * @param fill      Is the shape filled with a color, can be combined with draw.
     * @param fillColor Color to fill the shape with, color list in Color enum.
     * @param drawColor Outer line color, color list in Color enum.
     * @param shapeThicknessKey Thickness starting value.
     */
    public Shape(boolean draw, boolean fill, Color drawColor, Color fillColor, String shapeThicknessKey, int id) throws FatalException {
        this.draw = draw;
        this.fill = fill;

        if (fill) {
            Utility.checkObjects(fillColor);
            this.fillColor = fillColor;
        }
        if (draw) {
            Utility.checkObjects(drawColor);
            this.drawColor = drawColor;
        }
        setShapeThicknessKey(shapeThicknessKey);
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj instanceof Shape){
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getShapeThicknessKey() {
        return shapeThicknessKey.toLowerCase().replace("_", " ");
    }

    public Shape setShapeThicknessKey(String shapeThicknessKey) {
        this.shapeThicknessKey = shapeThicknessKey.toLowerCase().replace("_"," ");
        this.shapeThicknessValue = Thickness.valueOf(shapeThicknessKey).thicknessValue();
        return  this;
    }

    public double getShapeThicknessValue() {
        return shapeThicknessValue;
    }

    public String print() {
        String returnValue = "";
        if (this.fill && this.draw) {
            returnValue += "\\filldraw";
            returnValue += "[fill=" + this.fillColor.value + ", draw=" + this.drawColor.value + ", "+ this.shapeThicknessKey + "] " ;
        } else {
            if (this.fill) {
                returnValue += "\\fill";
                returnValue += "[fill=" + this.fillColor.value + ", "+ this.shapeThicknessKey + "] ";
            }
            if (this.draw) {
                returnValue += "\\draw";
                returnValue += "[draw=" + this.drawColor.value + ", "+ this.shapeThicknessKey + "] ";
            }
        }
        return returnValue;
    }

    public String printLabel() {
        if (this.label != null && this.label.length() > 0){
            Coordinates labelOffset = this.calcLabelOffset();
            return "node[align=center, right=" + labelOffset.getX() + "cm, above=" + labelOffset.getY() + "cm] {" + this.label + "}";
        }else{
            return "";
        }
    }

    public abstract Coordinates calcLabelOffset();
}
