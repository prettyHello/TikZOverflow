package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import java.io.Serializable;

/**
 * Represents the label that can be added to shapes
 */
public class LabelDTO implements Serializable {

    /**
     * The text of the label
     */
    private String value;

    /**
     * The color of the label
     */
    private ColorDTO color;

    public LabelDTO(String value, ColorDTO color) {
        this.value = value;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ColorDTO getColor() {
        return color;
    }

    public void setColor(ColorDTO color) {
        this.color = color;
    }
}
