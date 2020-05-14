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
    private Color color;

    public LabelDTO(String value, Color color) {
        this.value = value;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
