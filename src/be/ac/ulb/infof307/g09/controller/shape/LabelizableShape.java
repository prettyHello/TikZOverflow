package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.exceptions.FatalException;

public abstract class LabelizableShape extends Shape {

    private Label label;

    public LabelizableShape(boolean draw, boolean fill, Thickness thickness, int id) {
        super(draw, fill, thickness, id);
    }

    public LabelizableShape(boolean draw, boolean fill, Color drawColor, Color fillColor, Thickness thickness, int id) throws FatalException {
        super(draw, fill, drawColor, fillColor, thickness, id);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String print(){
        return super.print();
    }

    public String printLabel() {
        if (this.label != null && this.label.getValue().length() > 0){
            Coordinates labelOffset = this.calcLabelOffset();
            return " node[text=" + this.label.getColor().getValue() +", align=center, right=" + labelOffset.getX() + "cm, above=" + labelOffset.getY() + "cm] {" + this.label.getValue() + "}";
        }else{
            return "";
        }
    }

    public abstract Coordinates calcLabelOffset();
}
