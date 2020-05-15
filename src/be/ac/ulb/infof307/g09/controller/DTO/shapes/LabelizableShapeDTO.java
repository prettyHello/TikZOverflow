package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.exceptions.FatalException;

public abstract class LabelizableShapeDTO extends ShapeDTO {

    private LabelDTO label;

    public LabelizableShapeDTO(boolean draw, boolean fill, Thickness thickness, int id) {
        super(draw, fill, thickness, id);
    }

    public LabelizableShapeDTO(boolean draw, boolean fill, ColorDTO drawColor, ColorDTO fillColor, Thickness thickness, int id) throws FatalException {
        super(draw, fill, drawColor, fillColor, thickness, id);
    }

    public LabelDTO getLabel() {
        return label;
    }

    public void setLabel(LabelDTO label) {
        this.label = label;
    }

    public String printLabel() {
        if (this.label != null && this.label.getValue().length() > 0){
            CoordinatesDTO labelOffset = this.calcLabelOffset();
            return " node[text=" + this.label.getColor().getValue() +", align=center, right=" + labelOffset.getX() + "cm, above=" + labelOffset.getY() + "cm] {" + this.label.getValue() + "}";
        }else{
            return "";
        }
    }

    public abstract CoordinatesDTO calcLabelOffset();
}
